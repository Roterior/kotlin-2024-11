package ru.otus.otuskotlin.gasstation.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderDeleteRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.gasstation.app.spring.config.OrderConfig
import ru.otus.otuskotlin.gasstation.app.spring.controllers.OrderControllerV1
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.mappers.v1.toTransportCreate
import ru.otus.otuskotlin.gasstation.mappers.v1.toTransportDelete
import ru.otus.otuskotlin.gasstation.mappers.v1.toTransportRead
import ru.otus.otuskotlin.gasstation.mappers.v1.toTransportSearch
import ru.otus.otuskotlin.gasstation.mappers.v1.toTransportUpdate
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(OrderControllerV1::class, OrderConfig::class)
internal class OrderControllerV1Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: GsStOrderProcessor

    @Test
    fun testCreate() = testStubOrder(
        "/v1/order/create",
        OrderCreateRequest(),
        GsStContext().toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun testRead() = testStubOrder(
        "/v1/order/read",
        OrderReadRequest(),
        GsStContext().toTransportRead().copy(responseType = "read")
    )

    @Test
    fun testUpdate() = testStubOrder(
        "/v1/order/update",
        OrderUpdateRequest(),
        GsStContext().toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun testDelete() = testStubOrder(
        "/v1/order/delete",
        OrderDeleteRequest(),
        GsStContext().toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun testSearch() = testStubOrder(
        "/v1/order/search",
        OrderSearchRequest(),
        GsStContext().toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubOrder(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
