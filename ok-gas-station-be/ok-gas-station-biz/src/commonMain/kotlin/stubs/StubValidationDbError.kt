package ru.otus.otuskotlin.gasstation.biz.stubs

import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.common.models.GsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs

fun ICorChainDsl<GsStContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Заглушка ошибки базы данных
    """.trimIndent()
    on { stubCase == GsStStubs.DB_ERROR && state == GsStState.RUNNING }
    handle {
        fail(
            GsStError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
