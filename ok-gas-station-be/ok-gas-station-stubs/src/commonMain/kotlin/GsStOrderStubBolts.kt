package ru.otus.otuskotlin.gasstation.stubs

import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus

object GsStOrderStubBolts {
    val ORDER_AI_92_FUEL: GsStOrder
        get() = GsStOrder(
            id = GsStOrderId("123"),
            status = GsStStatus.CREATED,
            gasType = GsStGasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f,
            lock = GsStOrderLock("1"),
        )
    val ORDER_AI_95_FUEL = ORDER_AI_92_FUEL.copy(gasType = GsStGasType.AI_95)
}
