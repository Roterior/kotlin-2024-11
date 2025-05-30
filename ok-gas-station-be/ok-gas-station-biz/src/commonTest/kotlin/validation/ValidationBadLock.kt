package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationLockCorrect(command: GsStCommand, processor: GsStOrderProcessor) = runBizTest {
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

fun validationLockTrim(command: GsStCommand, processor: GsStOrderProcessor) = runBizTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrderStub.prepareResult {
            lock = GsStOrderLock(" \n\t 1 \n\t ")
        }
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GsStState.FAILING, ctx.state)
}

fun validationLockEmpty(command: GsStCommand, processor: GsStOrderProcessor) = runBizTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrderStub.prepareResult {
            lock = GsStOrderLock("")
        }
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GsStState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationLockFormat(command: GsStCommand, processor: GsStOrderProcessor) = runBizTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrderStub.prepareResult {
            lock = GsStOrderLock("!@#\$%^&*(),.{}")
        }
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GsStState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
