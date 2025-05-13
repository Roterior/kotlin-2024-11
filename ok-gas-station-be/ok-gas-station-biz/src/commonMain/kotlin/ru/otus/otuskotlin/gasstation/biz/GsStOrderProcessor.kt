package ru.otus.otuskotlin.gasstation.biz

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub

@Suppress("unused", "RedundantSuspendModifier")
class GsStOrderProcessor(val corSettings: GsStCorSettings) {
    suspend fun exec(ctx: GsStContext) {
        ctx.orderResponse = GsStOrderStub.get()
        ctx.ordersResponse = GsStOrderStub.prepareSearchList("order search", GsStGasType.AI_92).toMutableList()
        ctx.state = GsStState.RUNNING
    }
}
