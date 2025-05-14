package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker
import ru.otus.otuskotlin.gasstation.common.helpers.errorValidation
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId

fun ICorChainDsl<GsStContext>.validateIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { orderValidating.id != GsStOrderId.NONE && ! orderValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = orderValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
            field = "id",
            violationCode = "badFormat",
            description = "value $encodedId must contain only letters and numbers"
        )
        )
    }
}
