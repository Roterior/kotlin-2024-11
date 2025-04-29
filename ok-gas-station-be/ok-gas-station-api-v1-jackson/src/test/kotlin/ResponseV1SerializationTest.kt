package ru.otus.otuskotlin.gasstation.api.v1

import ru.otus.otuskotlin.gasstation.api.v1.models.GasType
import ru.otus.otuskotlin.gasstation.api.v1.models.IResponse
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderResponseObject
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderStatus
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {

    private val response = OrderCreateResponse(
        order = OrderResponseObject(
            status = OrderStatus.CREATED,
            gasType = GasType.AI_95,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"gasType\":\\s*\"AI_95\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as OrderCreateResponse

        assertEquals(response, obj)
    }
}
