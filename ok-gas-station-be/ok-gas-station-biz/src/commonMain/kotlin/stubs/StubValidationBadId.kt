package ru.otus.otuskotlin.gasstation.biz.stubs

import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.common.models.GsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs

fun ICorChainDsl<GsStContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Заглушка ошибки валидации идентификатора Заказа
    """.trimIndent()
    on { stubCase == GsStStubs.BAD_ID && state == GsStState.RUNNING }
    handle {
        fail(
            GsStError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
