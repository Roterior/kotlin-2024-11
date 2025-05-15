package ru.otus.otuskotlin.gasstation.app.common

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.gasstation.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.gasstation.api.v2.mappers.toTransportOrder
import ru.otus.otuskotlin.gasstation.api.v2.models.*
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = OrderCreateRequest(
        order = OrderCreateObject(
            status = OrderStatus.CREATED,
            gasType = GasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f
        ),
        debug = OrderDebug(mode = OrderRequestDebugMode.STUB, stub = OrderRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IGsStAppSettings = object : IGsStAppSettings {
        override val corSettings: GsStCorSettings = GsStCorSettings()
        override val processor: GsStOrderProcessor = GsStOrderProcessor(corSettings)
    }

    private suspend fun createSpring(request: OrderCreateRequest): OrderCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportOrder() as OrderCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createOrderKtor(appSettings: IGsStAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<OrderCreateRequest>()) },
            { toTransportOrder() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createOrderKtor(appSettings) }
        val res = testApp.res as OrderCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
