package ru.otus.otuskotlin.gasstation.biz.general

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.getOrderState(title: String) = worker {
    this.title = title
    this.description = """
        Получаем состояние из сервиса состояний
    """.trimIndent()
    on { state == GsStState.RUNNING }
    handle {
//        corSettings.stateSettings.stateMachine
    }
}
