package ru.otus.otuskotlin.gasstation.api.v2.mappers

import ru.otus.otuskotlin.gasstation.api.v2.models.GasType
import ru.otus.otuskotlin.gasstation.api.v2.models.IRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderCreateObject
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderCreateRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderDebug
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderDeleteObject
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderDeleteRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderReadObject
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderReadRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderRequestDebugMode
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderSearchFilter
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderSearchRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderStatus
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderUpdateObject
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderUpdateRequest
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderFilter
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs

fun GsStContext.fromTransport(request: IRequest) = when (request) {
    is OrderCreateRequest -> fromTransport(request)
    is OrderReadRequest -> fromTransport(request)
    is OrderUpdateRequest -> fromTransport(request)
    is OrderDeleteRequest -> fromTransport(request)
    is OrderSearchRequest -> fromTransport(request)
}

private fun String?.toOrderId() = this?.let { GsStOrderId(it) } ?: GsStOrderId.NONE
private fun String?.toOrderLock() = this?.let { GsStOrderLock(it) } ?: GsStOrderLock.NONE
private fun OrderReadObject?.toInternal() = if (this != null) {
    GsStOrder(id = id.toOrderId())
} else {
    GsStOrder()
}

private fun OrderDebug?.transportToWorkMode(): GsStWorkMode = when (this?.mode) {
    OrderRequestDebugMode.PROD -> GsStWorkMode.PROD
    OrderRequestDebugMode.TEST -> GsStWorkMode.TEST
    OrderRequestDebugMode.STUB -> GsStWorkMode.STUB
    null -> GsStWorkMode.PROD
}

private fun OrderDebug?.transportToStubCase(): GsStStubs = when (this?.stub) {
    OrderRequestDebugStubs.SUCCESS -> GsStStubs.SUCCESS
    OrderRequestDebugStubs.NOT_FOUND -> GsStStubs.NOT_FOUND
    OrderRequestDebugStubs.BAD_ID -> GsStStubs.BAD_ID
    OrderRequestDebugStubs.CANNOT_DELETE -> GsStStubs.CANNOT_DELETE
    OrderRequestDebugStubs.BAD_SEARCH_STRING -> GsStStubs.BAD_SEARCH_STRING
    null -> GsStStubs.NONE
}

fun GsStContext.fromTransport(request: OrderCreateRequest) {
    command = GsStCommand.CREATE
    orderRequest = request.order?.toInternal() ?: GsStOrder()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun GsStContext.fromTransport(request: OrderReadRequest) {
    command = GsStCommand.READ
    orderRequest = request.order.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun GsStContext.fromTransport(request: OrderUpdateRequest) {
    command = GsStCommand.UPDATE
    orderRequest = request.order?.toInternal() ?: GsStOrder()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun GsStContext.fromTransport(request: OrderDeleteRequest) {
    command = GsStCommand.DELETE
    orderRequest = request.order.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun OrderDeleteObject?.toInternal(): GsStOrder = if (this != null) {
    GsStOrder(
        id = id.toOrderId(),
        lock = lock.toOrderLock(),
    )
} else {
    GsStOrder()
}

fun GsStContext.fromTransport(request: OrderSearchRequest) {
    command = GsStCommand.SEARCH
    orderFilterRequest = request.orderFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun OrderSearchFilter?.toInternal(): GsStOrderFilter = GsStOrderFilter(
    searchString = this?.searchString ?: ""
)

private fun OrderCreateObject.toInternal(): GsStOrder = GsStOrder(
    status = status.fromTransport(),
    gasType = gasType.fromTransport(),
    price = price ?: 0f,
    quantity = quantity ?: 0f,
    summaryPrice = summaryPrice ?: 0f
)

private fun OrderUpdateObject.toInternal(): GsStOrder = GsStOrder(
    id = this.id.toOrderId(),
    lock = this.lock.toOrderLock(),

    status = status.fromTransport(),
    gasType = gasType.fromTransport(),
    price = price ?: 0f,
    quantity = quantity ?: 0f,
    summaryPrice = summaryPrice ?: 0f
)

private fun OrderStatus?.fromTransport(): GsStStatus = when (this) {
    OrderStatus.CREATED -> GsStStatus.CREATED
    OrderStatus.IN_PROCESS -> GsStStatus.IN_PROCESS
    OrderStatus.SUCCESS -> GsStStatus.SUCCESS
    OrderStatus.ERROR -> GsStStatus.ERROR
    null -> GsStStatus.NONE
}

private fun GasType?.fromTransport(): GsStGasType = when (this) {
    GasType.AI_92 -> GsStGasType.AI_92
    GasType.AI_95 -> GsStGasType.AI_95
    GasType.AI_98 -> GsStGasType.AI_98
    GasType.AI_100 -> GsStGasType.AI_100
    GasType.DIESEL -> GsStGasType.DIESEL
    null -> GsStGasType.NONE
}
