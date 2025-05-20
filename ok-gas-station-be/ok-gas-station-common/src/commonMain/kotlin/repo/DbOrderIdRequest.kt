package ru.otus.otuskotlin.gasstation.common.repo

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock

data class DbOrderIdRequest(
    val id: GsStOrderId,
    val lock: GsStOrderLock = GsStOrderLock.NONE,
) {
    constructor(order: GsStOrder): this(order.id, order.lock)
}
