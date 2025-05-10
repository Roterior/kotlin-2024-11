package ru.otus.otuskotlin.gasstation.api.v2

import ru.otus.otuskotlin.gasstation.api.v2.models.GasType
import ru.otus.otuskotlin.gasstation.api.v2.models.IRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderCreateObject
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderCreateRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderDebug
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderRequestDebugMode
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderStatus
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {

    private val request: IRequest = OrderCreateRequest(
        debug = OrderDebug(
            mode = OrderRequestDebugMode.STUB,
            stub = OrderRequestDebugStubs.BAD_ID
        ),
        order = OrderCreateObject(
            status = OrderStatus.CREATED,
            gasType = GasType.AI_95,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"gasType\":\\s*\"AI_95\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badId\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as OrderCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            { "order": null }
            """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<OrderCreateRequest>(jsonString)

        assertEquals(null, obj.order)
    }
}
