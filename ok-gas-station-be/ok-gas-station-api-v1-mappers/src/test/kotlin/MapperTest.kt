import org.junit.Test
import ru.otus.otuskotlin.gasstation.api.v1.models.GasType
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderDebug
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderStatus
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStRequestId
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs
import ru.otus.otuskotlin.gasstation.mappers.v1.fromTransport
import ru.otus.otuskotlin.gasstation.mappers.v1.toTransportCreate
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromTransport() {
        val req = OrderCreateRequest(
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            ),
            order = OrderCreateObject(
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
        assertEquals(GsStStatus.CREATED, context.orderRequest.status)
        assertEquals(GsStGasType.AI_95, context.orderRequest.gasType)
        assertEquals(10f, context.orderRequest.price)
        assertEquals(2f, context.orderRequest.quantity)
        assertEquals(20f, context.orderRequest.summaryPrice)
    }

    @Test
    fun toTransport() {
        val context = GsStContext(
            requestId = GsStRequestId("1234"),
            command = GsStCommand.CREATE,
            orderResponse = GsStOrder(
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

        val req = context.toTransportCreate()

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
