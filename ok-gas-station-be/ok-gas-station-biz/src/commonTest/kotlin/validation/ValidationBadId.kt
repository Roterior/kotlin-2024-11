package ru.otus.otuskotlin.gasstation.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.*
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: GsStCommand, processor: GsStOrderProcessor) = runBizTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrderStub.get()
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GsStState.FAILING, ctx.state)
}

fun validationIdTrim(command: GsStCommand, processor: GsStOrderProcessor) = runBizTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrderStub.prepareResult {
            id = GsStOrderId(" \n\t ${id.asString()} \n\t ")
        }
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GsStState.FAILING, ctx.state)
}

fun validationIdEmpty(command: GsStCommand, processor: GsStOrderProcessor) = runBizTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrderStub.prepareResult {
            id = GsStOrderId("")
        }
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GsStState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: GsStCommand, processor: GsStOrderProcessor) = runBizTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrderStub.prepareResult {
            id = GsStOrderId("!@#\$%^&*(),.{}")
        }
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GsStState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
