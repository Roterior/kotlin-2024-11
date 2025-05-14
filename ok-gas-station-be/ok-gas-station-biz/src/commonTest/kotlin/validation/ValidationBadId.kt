package ru.otus.otuskotlin.gasstation.biz.validation

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: GsStCommand, processor: GsStOrderProcessor) = runTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrder(
            id = GsStOrderId("123-234-abc-ABC"),
            status = GsStStatus.CREATED,
            gasType = GsStGasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f,
            lock = GsStOrderLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GsStState.FAILING, ctx.state)
}

fun validationIdTrim(command: GsStCommand, processor: GsStOrderProcessor) = runTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrder(
            id = GsStOrderId(" \n\t 123-234-abc-ABC \n\t "),
            status = GsStStatus.CREATED,
            gasType = GsStGasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f,
            lock = GsStOrderLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(GsStState.FAILING, ctx.state)
}

fun validationIdEmpty(command: GsStCommand, processor: GsStOrderProcessor) = runTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrder(
            id = GsStOrderId(""),
            status = GsStStatus.CREATED,
            gasType = GsStGasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f,
            lock = GsStOrderLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GsStState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: GsStCommand, processor: GsStOrderProcessor) = runTest {
    val ctx = GsStContext(
        command = command,
        state = GsStState.NONE,
        workMode = GsStWorkMode.TEST,
        orderRequest = GsStOrder(
            id = GsStOrderId("!@#\$%^&*(),.{}"),
            status = GsStStatus.CREATED,
            gasType = GsStGasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f,
            lock = GsStOrderLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(GsStState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
