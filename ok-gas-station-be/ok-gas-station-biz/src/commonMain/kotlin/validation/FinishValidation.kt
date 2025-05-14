package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.finishOrderValidation(title: String) = worker {
    this.title = title
    on { state == GsStState.RUNNING }
    handle {
        orderValidated = orderValidating
    }
}

fun ICorChainDsl<GsStContext>.finishOrderFilterValidation(title: String) = worker {
    this.title = title
    on { state == GsStState.RUNNING }
    handle {
        orderFilterValidated = orderFilterValidating
    }
}
