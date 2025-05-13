package ru.otus.otuskotlin.gasstation.api.v2.mappers

import ru.otus.otuskotlin.gasstation.api.v2.models.GasType
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderDebug
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderRequestDebugMode
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderStatus
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderUpdateObject
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderUpdateRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderUpdateResponse
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStRequestId
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperUpdateTest {

    @Test
    fun fromTransport() {
        val req = OrderUpdateRequest(
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            ),
            order = OrderUpdateObject(
                id = "1",
                lock = "2",
                status = OrderStatus.CREATED,
                gasType = GasType.AI_95,
                price = 10f,
                quantity = 2f,
                summaryPrice = 20f
            )
        )

        val context = GsStContext()
        context.fromTransport(req)

        assertEquals(GsStStubs.SUCCESS, context.stubCase)
        assertEquals(GsStWorkMode.STUB, context.workMode)
        assertEquals("1", context.orderRequest.id.asString())
        assertEquals("2", context.orderRequest.lock.asString())
        assertEquals(GsStStatus.CREATED, context.orderRequest.status)
        assertEquals(GsStGasType.AI_95, context.orderRequest.gasType)
        assertEquals(10f, context.orderRequest.price)
        assertEquals(2f, context.orderRequest.quantity)
        assertEquals(20f, context.orderRequest.summaryPrice)
    }

    @Test
    fun toTransport() {
        val context = GsStContext(
            requestId = GsStRequestId("123"),
            command = GsStCommand.UPDATE,
            orderResponse = GsStOrder(
                id = GsStOrderId("1"),
                lock = GsStOrderLock("2"),
                status = GsStStatus.CREATED,
                gasType = GsStGasType.AI_95,
                price = 10f,
                quantity = 2f,
                summaryPrice = 20f
            ),
            errors = mutableListOf(
                GsStError(
                    code = "400",
                    group = "request",
                    field = "price",
                    message = "wrong price"
                )
            ),
            state = GsStState.RUNNING
        )

        val req = context.toTransportOrder() as OrderUpdateResponse

        assertEquals("1", req.order?.id)
        assertEquals("2", req.order?.lock)
        assertEquals(OrderStatus.CREATED, req.order?.status)
        assertEquals(GasType.AI_95, req.order?.gasType)
        assertEquals(10f, req.order?.price)
        assertEquals(2f, req.order?.quantity)
        assertEquals(20f, req.order?.summaryPrice)

        assertEquals(1, req.errors?.size)
        assertEquals("400", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("price", req.errors?.firstOrNull()?.field)
        assertEquals("wrong price", req.errors?.firstOrNull()?.message)
    }
}
