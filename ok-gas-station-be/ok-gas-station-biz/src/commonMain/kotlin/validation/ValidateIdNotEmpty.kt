package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker
import ru.otus.otuskotlin.gasstation.common.helpers.errorValidation
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail

fun ICorChainDsl<GsStContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { orderValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
