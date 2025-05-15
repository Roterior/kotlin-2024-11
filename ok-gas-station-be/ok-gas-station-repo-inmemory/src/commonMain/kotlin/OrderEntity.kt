package ru.otus.otuskotlin.gasstation.repo.inmemory

import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus

data class OrderEntity(
    val id: String? = null,
    val status: String? = null,
    val gasType: String? = null,
    val price: String? = null,
    val quantity: String? = null,
    val summaryPrice: String? = null,
    val lock: String? = null
) {
    constructor(model: GsStOrder) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        status = model.status.takeIf { it != GsStStatus.NONE }?.name,
        gasType = model.gasType.takeIf { it != GsStGasType.NONE }?.name,
        price = model.price.takeIf { it != 0f }.toString(),
        quantity = model.quantity.takeIf { it != 0f }.toString(),
        summaryPrice = model.summaryPrice.takeIf { it != 0f }.toString(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = GsStOrder(
        id = id?.let { GsStOrderId(it) } ?: GsStOrderId.NONE,
        status = status?.let { GsStStatus.valueOf(it) } ?: GsStStatus.NONE,
        gasType = gasType?.let { GsStGasType.valueOf(it) } ?: GsStGasType.NONE,
        price = price?.toFloatOrNull() ?: 0f,
        quantity = quantity?.toFloatOrNull() ?: 0f,
        summaryPrice = summaryPrice?.toFloatOrNull() ?: 0f,
        lock = lock?.let { GsStOrderLock(it) } ?: GsStOrderLock.NONE
    )
}
