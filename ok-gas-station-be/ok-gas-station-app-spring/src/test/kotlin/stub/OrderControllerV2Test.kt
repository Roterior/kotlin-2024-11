package ru.otus.otuskotlin.gasstation.app.spring.stub

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.gasstation.app.spring.config.OrderConfig
import ru.otus.otuskotlin.gasstation.app.spring.controllers.OrderControllerV2
import ru.otus.otuskotlin.gasstation.api.v2.mappers.*
import ru.otus.otuskotlin.gasstation.api.v2.models.*
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStContext
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(OrderControllerV2::class, OrderConfig::class)
internal class OrderControllerV2Test {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: GsStOrderProcessor

    @Test
    fun testCreate() = testStubOrder(
        "/v2/order/create",
        OrderCreateRequest(),
        GsStContext().toTransportCreate()
    )

    @Test
    fun testRead() = testStubOrder(
        "/v2/order/read",
        OrderReadRequest(),
        GsStContext().toTransportRead()
    )

    @Test
    fun testUpdate() = testStubOrder(
        "/v2/order/update",
        OrderUpdateRequest(),
        GsStContext().toTransportUpdate()
    )

    @Test
    fun testDelete() = testStubOrder(
        "/v2/order/delete",
        OrderDeleteRequest(),
        GsStContext().toTransportDelete()
    )

    @Test
    fun testSearch() = testStubOrder(
        "/v2/order/search",
        OrderSearchRequest(),
        GsStContext().toTransportSearch()
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
