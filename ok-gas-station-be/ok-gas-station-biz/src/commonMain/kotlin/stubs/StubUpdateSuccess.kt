package ru.otus.otuskotlin.gasstation.biz.stubs

import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs
import ru.otus.otuskotlin.gasstation.logging.common.LogLevel
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub

fun ICorChainDsl<GsStContext>.stubUpdateSuccess(title: String, corSettings: GsStCorSettings) = worker {
    this.title = title
    this.description = """
        Заглушка успешного обновления Заказа
    """.trimIndent()
    on { stubCase == GsStStubs.SUCCESS && state == GsStState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubUpdateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = GsStState.FINISHING
            val stub = GsStOrderStub.prepareResult {
                orderRequest.id.takeIf { it != GsStOrderId.NONE }?.also { this.id = it }
                orderRequest.status.takeIf { it != GsStStatus.NONE }?.also { this.status = it }
                orderRequest.gasType.takeIf { it != GsStGasType.NONE }?.also { this.gasType = it }
                orderRequest.price.takeIf { it != 0f }?.also { this.price = it }
                orderRequest.quantity.takeIf { it != 0f }?.also { this.quantity = it }
                orderRequest.summaryPrice.takeIf { it != 0f }?.also { this.summaryPrice = it }
            }
            orderResponse = stub
        }
    }
}
