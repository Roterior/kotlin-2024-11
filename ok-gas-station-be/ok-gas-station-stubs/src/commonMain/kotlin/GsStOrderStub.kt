package ru.otus.otuskotlin.gasstation.stubs

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStubBolts.ORDER_AI_92_FUEL
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStubBolts.ORDER_AI_95_FUEL

object GsStOrderStub {
    fun get(): GsStOrder = ORDER_AI_92_FUEL.copy()

    fun prepareResult(block: GsStOrder.() -> Unit): GsStOrder = get().apply(block)

    fun prepareSearchList(filter: String, type: GsStGasType) = listOf(
        gsStOrderAi92("ai1231", filter, type),
        gsStOrderAi92("ai1232", filter, type),
        gsStOrderAi92("ai1233", filter, type),
        gsStOrderAi92("ai1234", filter, type),
        gsStOrderAi92("ai1235", filter, type),
        gsStOrderAi92("ai1236", filter, type),
    )

    private fun gsStOrderAi92(id: String, filter: String, type: GsStGasType) =
        gsStOrder(ORDER_AI_92_FUEL, id = id, filter = filter, type = type)

    private fun gsStOrderAi95(id: String, filter: String, type: GsStGasType) =
        gsStOrder(ORDER_AI_95_FUEL, id = id, filter = filter, type = type)

    private fun gsStOrder(base: GsStOrder, id: String, filter: String, type: GsStGasType) = base.copy(
        id = GsStOrderId(id),
        gasType = type
    )
}
