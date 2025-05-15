package ru.otus.otuskotlin.gasstation.stubs

import ru.otus.otuskotlin.gasstation.common.models.*

object GsStOrderStubBolts {
    val ORDER_AI_92_FUEL: GsStOrder
        get() = GsStOrder(
            id = GsStOrderId("123"),
            status = GsStStatus.CREATED,
            gasType = GsStGasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f
        )
    val ORDER_AI_95_FUEL = ORDER_AI_92_FUEL.copy(gasType = GsStGasType.AI_95)
}
