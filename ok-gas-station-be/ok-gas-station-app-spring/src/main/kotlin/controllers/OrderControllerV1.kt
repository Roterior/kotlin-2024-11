package ru.otus.otuskotlin.gasstation.app.spring.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.gasstation.api.v1.models.IRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.IResponse
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderDeleteRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderDeleteResponse
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderReadRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderReadResponse
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderSearchRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderSearchResponse
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderUpdateRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderUpdateResponse
import ru.otus.otuskotlin.gasstation.app.common.controllerHelper
import ru.otus.otuskotlin.gasstation.app.spring.config.GsStAppSettings
import ru.otus.otuskotlin.gasstation.mappers.v1.fromTransport
import ru.otus.otuskotlin.gasstation.mappers.v1.toTransportOrder
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/order")
class OrderControllerV1(
    private val appSettings: GsStAppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: OrderCreateRequest): OrderCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun read(@RequestBody request: OrderReadRequest): OrderReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun update(@RequestBody request: OrderUpdateRequest): OrderUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun delete(@RequestBody request: OrderDeleteRequest): OrderDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun search(@RequestBody request: OrderSearchRequest): OrderSearchResponse =
        process(appSettings, request = request, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: GsStAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportOrder() as R },
            clazz,
            logId,
        )
    }
}
