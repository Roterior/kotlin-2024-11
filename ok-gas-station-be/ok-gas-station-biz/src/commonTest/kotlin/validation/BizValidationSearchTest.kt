package ru.otus.otuskotlin.gasstation.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderFilter
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = GsStCommand.SEARCH

    @Test
    fun correctEmpty() = runTest {
        val ctx = GsStContext(
            command = command,
            state = GsStState.NONE,
            workMode = GsStWorkMode.TEST,
            orderFilterRequest = GsStOrderFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(GsStState.FAILING, ctx.state)
    }
}
