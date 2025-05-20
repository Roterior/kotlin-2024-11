package ru.otus.otuskotlin.gasstation.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderIdRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.gasstation.common.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.gasstation.common.repo.IDbOrderResponse
import ru.otus.otuskotlin.gasstation.common.repo.IDbOrdersResponse
import ru.otus.otuskotlin.gasstation.common.repo.IRepoOrder
import ru.otus.otuskotlin.gasstation.common.repo.OrderRepoBase
import ru.otus.otuskotlin.gasstation.common.repo.errorDb
import ru.otus.otuskotlin.gasstation.common.repo.errorEmptyId
import ru.otus.otuskotlin.gasstation.common.repo.errorEmptyLock
import ru.otus.otuskotlin.gasstation.common.repo.errorNotFound
import ru.otus.otuskotlin.gasstation.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.gasstation.common.repo.exceptions.RepoEmptyLockException
import ru.otus.otuskotlin.gasstation.repo.common.IRepoOrderInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class OrderRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : OrderRepoBase(), IRepoOrder, IRepoOrderInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, OrderEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(orders: Collection<GsStOrder>) = orders.map { order ->
        val entity = OrderEntity(order)
        require(entity.id != null)
        cache.put(entity.id, entity)
        order
    }

    override suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse = tryOrderMethod {
        val key = randomUuid()
        val order = rq.order.copy(id = GsStOrderId(key), lock = GsStOrderLock(randomUuid()))
        val entity = OrderEntity(order)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbOrderResponseOk(order)
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse = tryOrderMethod {
        val key = rq.id.takeIf { it != GsStOrderId.NONE }?.asString() ?: return@tryOrderMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbOrderResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse = tryOrderMethod {
        val rqOrder = rq.order
        val id = rqOrder.id.takeIf { it != GsStOrderId.NONE } ?: return@tryOrderMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqOrder.lock.takeIf { it != GsStOrderLock.NONE } ?: return@tryOrderMethod errorEmptyLock(id)

        mutex.withLock {
            val oldOrder = cache.get(key)?.toInternal()
            when {
                oldOrder == null -> errorNotFound(id)
                oldOrder.lock == GsStOrderLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldOrder.lock != oldLock -> errorRepoConcurrency(oldOrder, oldLock)
                else -> {
                    val newOrder = rqOrder.copy(lock = GsStOrderLock(randomUuid()))
                    val entity = OrderEntity(newOrder)
                    cache.put(key, entity)
                    DbOrderResponseOk(newOrder)
                }
            }
        }
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse = tryOrderMethod {
        val id = rq.id.takeIf { it != GsStOrderId.NONE } ?: return@tryOrderMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != GsStOrderLock.NONE } ?: return@tryOrderMethod errorEmptyLock(id)

        mutex.withLock {
            val oldOrder = cache.get(key)?.toInternal()
            when {
                oldOrder == null -> errorNotFound(id)
                oldOrder.lock == GsStOrderLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldOrder.lock != oldLock -> errorRepoConcurrency(oldOrder, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbOrderResponseOk(oldOrder)
                }
            }
        }
    }

    /**
     * Поиск по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse = tryOrdersMethod {
        val result: List<GsStOrder> = cache.asMap().asSequence()
            .filter { entry ->
                rq.status.takeIf { it != GsStStatus.NONE }?.let {
                    it.name == entry.value.status
                } ?: true
            }
            .filter { entry ->
                rq.gasType.takeIf { it != GsStGasType.NONE }?.let {
                    it.name == entry.value.gasType
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbOrdersResponseOk(result)
    }
}
