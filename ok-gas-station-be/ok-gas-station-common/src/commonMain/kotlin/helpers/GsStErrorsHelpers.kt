package ru.otus.otuskotlin.gasstation.common.helpers

import ru.otus.otuskotlin.gasstation.common.models.GsStError

fun Throwable.asGsStError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = GsStError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
