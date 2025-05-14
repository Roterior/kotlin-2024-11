package ru.otus.otuskotlin.gasstation.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderFilter
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.cor.rootChain
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidateSearchStringLengthTest {
    @Test
    fun emptyString() = runTest {
        val ctx = GsStContext(state = GsStState.RUNNING, orderFilterValidating = GsStOrderFilter(searchString = ""))
        chain.exec(ctx)
        assertEquals(GsStState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun blankString() = runTest {
        val ctx = GsStContext(state = GsStState.RUNNING, orderFilterValidating = GsStOrderFilter(searchString = "  "))
        chain.exec(ctx)
        assertEquals(GsStState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun shortString() = runTest {
        val ctx = GsStContext(state = GsStState.RUNNING, orderFilterValidating = GsStOrderFilter(searchString = "12"))
        chain.exec(ctx)
        assertEquals(GsStState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooShort", ctx.errors.first().code)
    }

    @Test
    fun normalString() = runTest {
        val ctx = GsStContext(state = GsStState.RUNNING, orderFilterValidating = GsStOrderFilter(searchString = "123"))
        chain.exec(ctx)
        assertEquals(GsStState.RUNNING, ctx.state)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun longString() = runTest {
        val ctx = GsStContext(state = GsStState.RUNNING, orderFilterValidating = GsStOrderFilter(searchString = "12".repeat(51)))
        chain.exec(ctx)
        assertEquals(GsStState.FAILING, ctx.state)
        assertEquals(1, ctx.errors.size)
        assertEquals("validation-searchString-tooLong", ctx.errors.first().code)
    }

    companion object {
        val chain = rootChain {
            validateSearchStringLength("")
        }.build()
    }
}
