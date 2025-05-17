package ru.otus.otuskotlin.gasstation.backend.repo.tests

import ru.otus.otuskotlin.gasstation.common.models.*

abstract class BaseInitOrders(private val op: String): IInitObjects<GsStOrder> {
    open val lockOld: GsStOrderLock = GsStOrderLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: GsStOrderLock = GsStOrderLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        status: GsStStatus = GsStStatus.CREATED,
        gasType: GsStGasType = GsStGasType.AI_92,
        lock: GsStOrderLock = lockOld
    ) = GsStOrder(
        id = GsStOrderId("order-repo-$op-$suf"),
        status = status,
        gasType = gasType,
        price = 10f,
        quantity = 2f,
        summaryPrice = 20f,
        lock = lock
    )
}
