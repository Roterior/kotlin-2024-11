package ru.otus.otuskotlin.gasstation.common.repo.exceptions

import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId

open class RepoOrderException(
    @Suppress("unused")
    val orderId: GsStOrderId,
    msg: String,
): RepoException(msg)
