package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.chain

fun ICorChainDsl<GsStContext>.validation(block: ICorChainDsl<GsStContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == GsStState.RUNNING }
}
