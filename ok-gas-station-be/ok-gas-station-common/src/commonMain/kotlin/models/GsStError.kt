package ru.otus.otuskotlin.gasstation.common.models

data class GsStError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)

