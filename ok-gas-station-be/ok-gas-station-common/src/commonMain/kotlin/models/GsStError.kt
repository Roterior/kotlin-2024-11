package ru.otus.otuskotlin.gasstation.common.models

import ru.otus.otuskotlin.gasstation.logging.common.LogLevel

data class GsStError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)

