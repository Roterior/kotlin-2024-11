package ru.otus.otuskotlin.gasstation.api.v2

import ru.otus.otuskotlin.gasstation.api.v2.models.GasType
import ru.otus.otuskotlin.gasstation.api.v2.models.IResponse
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderCreateResponse
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderResponseObject
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderStatus
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {

    private val response: IResponse = OrderCreateResponse(
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
        val json = apiV2Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"gasType\":\\s*\"AI_95\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json) as OrderCreateResponse

        assertEquals(response, obj)
    }
}
