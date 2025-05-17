package ru.otus.otuskotlin.gasstation.biz.stub

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.*
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class OrderSearchStubTest {

    private val processor = GsStOrderProcessor()
    val filter = GsStOrderFilter(status = GsStStatus.CREATED)

    @Test
    fun search() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.SEARCH,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.SUCCESS,
            orderFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.ordersResponse.size > 1)
        val first = ctx.ordersResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.status == filter.status)
        with (GsStOrderStub.get()) {
            assertEquals(status, first.status)
            assertEquals(gasType, first.gasType)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.SEARCH,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.BAD_ID,
            orderFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.SEARCH,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.DB_ERROR,
            orderFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = GsStContext(
            command = GsStCommand.SEARCH,
            state = GsStState.NONE,
            workMode = GsStWorkMode.STUB,
            stubCase = GsStStubs.BAD_SEARCH_STRING,
            orderFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(GsStOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
