package ru.otus.otuskotlin.gasstation.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.otus.otuskotlin.gasstation.common.helpers.asGsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderIdRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.gasstation.common.repo.DbOrdersResponseErr
import ru.otus.otuskotlin.gasstation.common.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.gasstation.common.repo.IDbOrderResponse
import ru.otus.otuskotlin.gasstation.common.repo.IDbOrdersResponse
import ru.otus.otuskotlin.gasstation.common.repo.IRepoOrder
import ru.otus.otuskotlin.gasstation.common.repo.errorEmptyId
import ru.otus.otuskotlin.gasstation.common.repo.errorNotFound
import ru.otus.otuskotlin.gasstation.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.gasstation.repo.common.IRepoOrderInitializable

class RepoOrderSql(
    properties: SqlProperties,
    private val randomUuid: () -> String = { uuid4().toString() },
    private val lockUuid: () -> String = { "57d6e572-2ff1-4a95-84df-efafd4d78d0f" }
) : IRepoOrder, IRepoOrderInitializable {

    private val orderTable = OrderTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    fun clear(): Unit = transaction(conn) {
        orderTable.deleteAll()
    }

    private fun saveObj(order: GsStOrder): GsStOrder = transaction(conn) {
        val res = orderTable
            .insert {
                to(it, order, randomUuid, lockUuid)
            }
            .resultedValues
            ?.map { orderTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(
        crossinline block: () -> T,
        crossinline handle: (Exception) -> T
    ): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbOrderResponse): IDbOrderResponse =
        transactionWrapper(block) { DbOrderResponseErr(it.asGsStError()) }

    override fun save(orders: Collection<GsStOrder>): Collection<GsStOrder> = orders.map { saveObj(it) }

    override suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse = transactionWrapper {
        DbOrderResponseOk(saveObj(rq.order))
    }

    private fun read(id: GsStOrderId): IDbOrderResponse {
        val res = orderTable.selectAll().where {
            orderTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbOrderResponseOk(orderTable.from(res))
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: GsStOrderId,
        lock: GsStOrderLock,
        block: (GsStOrder) -> IDbOrderResponse
    ): IDbOrderResponse =
        transactionWrapper {
            if (id == GsStOrderId.NONE) return@transactionWrapper errorEmptyId

            val current = orderTable.selectAll().where { orderTable.id eq id.asString() }
                .singleOrNull()
                ?.let { orderTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    override suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse = update(rq.order.id, rq.order.lock) {
        orderTable.update({ orderTable.id eq rq.order.id.asString() }) {
            to(it, rq.order.copy(lock = GsStOrderLock(lockUuid())), randomUuid, lockUuid)
        }
        read(rq.order.id)
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse = update(rq.id, rq.lock) {
        orderTable.deleteWhere { id eq rq.id.asString() }
        DbOrderResponseOk(it)
    }

    override suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse =
        transactionWrapper({
            val res = orderTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.status != GsStStatus.NONE) {
                        add(orderTable.status eq rq.status)
                    }
                    if (rq.gasType != GsStGasType.NONE) {
                        add(orderTable.gasType eq rq.gasType)
                    }
                }.reduce { a, b -> a and b }
            }
            DbOrdersResponseOk(data = res.map { orderTable.from(it) })
        }, {
            DbOrdersResponseErr(it.asGsStError())
        })
}
