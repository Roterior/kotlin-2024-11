package ru.otus.otuskotlin.gasstation.backend.repo.tests

import ru.otus.otuskotlin.gasstation.common.models.*
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.gasstation.repo.common.OrderRepoInitialized
import kotlin.test.*

abstract class RepoOrderCreateTest {

    abstract val repo: OrderRepoInitialized
    protected open val lockNew = GsStOrderLock("20000000-0000-0000-0000-000000000002")

    private val createObj = GsStOrder(
        status = GsStStatus.CREATED,
        gasType = GsStGasType.AI_92,
        price = 10f,
        quantity = 2f,
        summaryPrice = 20f
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createOrder(DbOrderRequest(createObj))
        val expected = createObj
        assertIs<DbOrderResponseOk>(result)
        assertNotEquals(GsStOrderId.NONE, result.data.id)
        assertEquals(lockNew, result.data.lock)
        assertEquals(expected.status, result.data.status)
        assertEquals(expected.gasType, result.data.gasType)
        assertEquals(expected.price, result.data.price)
        assertNotEquals(GsStOrderId.NONE, result.data.id)
    }

    companion object : BaseInitOrders("create") {
        override val initObjects: List<GsStOrder> = emptyList()
    }
}
