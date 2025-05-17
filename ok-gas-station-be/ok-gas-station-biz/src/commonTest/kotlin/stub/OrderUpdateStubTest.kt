package ru.otus.otuskotlin.gasstation.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.*
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderUpdateStubTest {

    private val processor = GsStOrderProcessor()
    val id = GsStOrderId("123")
    var status = GsStStatus.CREATED
    var gasType = GsStGasType.AI_92
    var price = 10f
    var quantity = 2f
    var summaryPrice = 20f

    @Test
    fun update() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.UPDATE,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.SUCCESS,
            orderRequest = GsStOrder(
                id = id,
                status = status,
                gasType = gasType,
                price = price,
                quantity = quantity,
                summaryPrice = summaryPrice
            )
        )
        processor.exec(ctx)
        assertEquals(id, ctx.orderResponse.id)
        assertEquals(status, ctx.orderResponse.status)
        assertEquals(gasType, ctx.orderResponse.gasType)
        assertEquals(price, ctx.orderResponse.price)
        assertEquals(quantity, ctx.orderResponse.quantity)
        assertEquals(summaryPrice, ctx.orderResponse.summaryPrice)
    }

    @Test
    fun badId() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.UPDATE,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.BAD_ID,
            orderRequest = GsStOrder()
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.UPDATE,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.DB_ERROR,
            orderRequest = GsStOrder(
                id = id
            )
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.UPDATE,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.BAD_SEARCH_STRING,
            orderRequest = GsStOrder(
                id = id,
                status = status,
                gasType = gasType,
                price = price,
                quantity = quantity,
                summaryPrice = summaryPrice
            )
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
