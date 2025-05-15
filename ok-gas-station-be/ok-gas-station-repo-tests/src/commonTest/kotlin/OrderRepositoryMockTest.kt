package ru.otus.otuskotlin.gasstation.backend.repo.tests

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.repo.*
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class OrderRepositoryMockTest {

    private val repo = OrderRepositoryMock(
        invokeCreateOrder = { DbOrderResponseOk(GsStOrderStub.prepareResult { gasType = GsStGasType.AI_92 }) },
        invokeReadOrder = { DbOrderResponseOk(GsStOrderStub.prepareResult { gasType = GsStGasType.AI_95 }) },
        invokeUpdateOrder = { DbOrderResponseOk(GsStOrderStub.prepareResult { gasType = GsStGasType.AI_98 }) },
        invokeDeleteOrder = { DbOrderResponseOk(GsStOrderStub.prepareResult { gasType = GsStGasType.AI_100 }) },
        invokeSearchOrder = { DbOrdersResponseOk(listOf(GsStOrderStub.prepareResult { gasType = GsStGasType.DIESEL })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createOrder(DbOrderRequest(GsStOrder()))
        assertIs<DbOrderResponseOk>(result)
        assertEquals(GsStGasType.AI_92, result.data.gasType)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readOrder(DbOrderIdRequest(GsStOrder()))
        assertIs<DbOrderResponseOk>(result)
        assertEquals(GsStGasType.AI_95, result.data.gasType)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateOrder(DbOrderRequest(GsStOrder()))
        assertIs<DbOrderResponseOk>(result)
        assertEquals(GsStGasType.AI_98, result.data.gasType)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteOrder(DbOrderIdRequest(GsStOrder()))
        assertIs<DbOrderResponseOk>(result)
        assertEquals(GsStGasType.AI_100, result.data.gasType)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchOrder(DbOrderFilterRequest())
        assertIs<DbOrdersResponseOk>(result)
        assertEquals(GsStGasType.DIESEL, result.data.first().gasType)
    }
}
