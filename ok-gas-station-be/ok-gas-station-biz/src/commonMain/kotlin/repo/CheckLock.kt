package ru.otus.otuskotlin.gasstation.biz.repo

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == GsStState.RUNNING && orderValidated.lock != orderRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(orderRepoRead, orderValidated.lock).errors)
    }
}
