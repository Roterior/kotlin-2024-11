package ru.otus.otuskotlin.gasstation.biz.stubs

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.chain

fun ICorChainDsl<GsStContext>.stubs(title: String, block: ICorChainDsl<GsStContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == GsStWorkMode.STUB && state == GsStState.RUNNING }
}
