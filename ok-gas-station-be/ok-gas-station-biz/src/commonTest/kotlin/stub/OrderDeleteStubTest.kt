package ru.otus.otuskotlin.gasstation.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.*
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderDeleteStubTest {

    private val processor = GsStOrderProcessor()
    val id = GsStOrderId("123")

    @Test
    fun delete() = runTest {

        val ctx = GsStContext(
            command = GsStCommand.DELETE,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.SUCCESS,
            orderRequest = GsStOrder(
                id = id
            )
        )
        processor.exec(ctx)

        val stub = GsStOrderStub.get()
        assertEquals(stub.id, ctx.orderResponse.id)
        assertEquals(stub.status, ctx.orderResponse.status)
        assertEquals(stub.gasType, ctx.orderResponse.gasType)
        assertEquals(stub.price, ctx.orderResponse.price)
        assertEquals(stub.quantity, ctx.orderResponse.quantity)
        assertEquals(stub.summaryPrice, ctx.orderResponse.summaryPrice)
    }

    @Test
    fun badId() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.DELETE,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.BAD_ID,
            orderRequest = GsStOrder(),
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.DELETE,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.DB_ERROR,
            orderRequest = GsStOrder(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.DELETE,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.BAD_SEARCH_STRING,
            orderRequest = GsStOrder(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
