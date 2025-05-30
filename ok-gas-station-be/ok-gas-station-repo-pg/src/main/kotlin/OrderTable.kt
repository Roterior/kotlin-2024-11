package ru.otus.otuskotlin.gasstation.backend.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock

class OrderTable(tableName: String) : Table(tableName) {

    val id = text(SqlFields.ID)
    val status = statusEnum(SqlFields.STATUS)
    val gasType = gasTypeEnum(SqlFields.GAS_TYPE)
    val price = text(SqlFields.PRICE)
    val quantity = text(SqlFields.QUANTITY)
    val summaryPrice = text(SqlFields.SUMMARY_PRICE)
    val lock = text(SqlFields.LOCK)

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = GsStOrder(
        id = GsStOrderId(res[id].toString()),
        status = res[status],
        gasType = res[gasType],
        price = res[price].toFloat(),
        quantity = res[quantity].toFloat(),
        summaryPrice = res[summaryPrice].toFloat(),
        lock = GsStOrderLock(res[lock])
    )

    fun to(it: UpdateBuilder<*>, order: GsStOrder, randomUuid: () -> String, lockUuid: () -> String) {
        it[id] = order.id.takeIf { it != GsStOrderId.NONE }?.asString() ?: randomUuid()
        it[status] = order.status
        it[gasType] = order.gasType
        it[price] = order.price.toString()
        it[quantity] = order.quantity.toString()
        it[summaryPrice] = order.summaryPrice.toString()
        it[lock] = order.lock.takeIf { it != GsStOrderLock.NONE }?.asString() ?: lockUuid()
    }
}
