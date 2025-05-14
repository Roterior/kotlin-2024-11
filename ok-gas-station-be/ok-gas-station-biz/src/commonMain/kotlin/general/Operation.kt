package ru.otus.otuskotlin.gasstation.biz.general

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.chain

fun ICorChainDsl<GsStContext>.operation(
    title: String,
    command: GsStCommand,
    block: ICorChainDsl<GsStContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == GsStState.RUNNING }
}
