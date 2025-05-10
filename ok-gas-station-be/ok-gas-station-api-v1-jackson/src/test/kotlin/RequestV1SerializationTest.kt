package ru.otus.otuskotlin.gasstation.api.v1

import ru.otus.otuskotlin.gasstation.api.v1.models.GasType
import ru.otus.otuskotlin.gasstation.api.v1.models.IRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderDebug
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderStatus
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {

    private val request = OrderCreateRequest(
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
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"gasType\":\\s*\"AI_95\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badId\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as OrderCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            { "order": null }
            """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, OrderCreateRequest::class.java)

        assertEquals(null, obj.order)
    }
}
