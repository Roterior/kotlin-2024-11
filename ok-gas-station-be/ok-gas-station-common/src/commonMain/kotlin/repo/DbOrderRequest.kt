package ru.otus.otuskotlin.gasstation.common.repo

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder

data class DbOrderRequest(
    val order: GsStOrder
)
