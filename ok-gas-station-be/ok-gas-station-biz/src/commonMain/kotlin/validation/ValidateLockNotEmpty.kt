package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.common.helpers.errorValidation
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { orderValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
