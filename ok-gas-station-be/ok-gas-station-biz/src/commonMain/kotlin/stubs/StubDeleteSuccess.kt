package ru.otus.otuskotlin.gasstation.biz.stubs

import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs
import ru.otus.otuskotlin.gasstation.logging.common.LogLevel
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub

fun ICorChainDsl<GsStContext>.stubDeleteSuccess(title: String, corSettings: GsStCorSettings) = worker {
    this.title = title
    this.description = """
        Заглушка успешного удаления Заказа
    """.trimIndent()
    on { stubCase == GsStStubs.SUCCESS && state == GsStState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = GsStState.FINISHING
            val stub = GsStOrderStub.prepareResult {
                orderRequest.status.takeIf { it != GsStStatus.NONE }?.also { this.status = it }
            }
            orderResponse = stub
        }
    }
}
