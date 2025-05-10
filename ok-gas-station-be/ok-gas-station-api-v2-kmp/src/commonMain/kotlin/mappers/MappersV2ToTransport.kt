package ru.otus.otuskotlin.gasstation.api.v2.mappers

import ru.otus.otuskotlin.gasstation.api.v2.models.Error
import ru.otus.otuskotlin.gasstation.api.v2.models.GasType
import ru.otus.otuskotlin.gasstation.api.v2.models.IResponse
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderCreateResponse
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderDeleteResponse
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderReadResponse
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderResponseObject
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderSearchResponse
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderStatus
import ru.otus.otuskotlin.gasstation.api.v2.models.OrderUpdateResponse
import ru.otus.otuskotlin.gasstation.api.v2.models.ResponseResult
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.exceptions.UnknownGsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus

fun GsStContext.toTransportAd(): IResponse = when (val cmd = command) {
    GsStCommand.CREATE -> toTransportCreate()
    GsStCommand.READ -> toTransportRead()
    GsStCommand.UPDATE -> toTransportUpdate()
    GsStCommand.DELETE -> toTransportDelete()
    GsStCommand.SEARCH -> toTransportSearch()
    GsStCommand.NONE -> throw UnknownGsStCommand(cmd)
}

fun GsStContext.toTransportCreate() = OrderCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder()
)

fun GsStContext.toTransportRead() = OrderReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder()
)

fun GsStContext.toTransportUpdate() = OrderUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder()
)

fun GsStContext.toTransportDelete() = OrderDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder()
)

fun GsStContext.toTransportSearch() = OrderSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    orders = ordersResponse.toTransportOrder()
)

fun List<GsStOrder>.toTransportOrder(): List<OrderResponseObject>? = this
    .map { it.toTransportOrder() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun GsStOrder.toTransportOrder(): OrderResponseObject = OrderResponseObject(
    id = id.takeIf { it != GsStOrderId.NONE }?.asString(),
    lock = lock.takeIf { it != GsStOrderLock.NONE }?.asString(),

    status = status.toTransportOrder(),
    gasType = gasType.toTransportOrder(),
    price = price,
    quantity = quantity,
    summaryPrice = summaryPrice
)

private fun GsStStatus.toTransportOrder(): OrderStatus? = when (this) {
    GsStStatus.CREATED -> OrderStatus.CREATED
    GsStStatus.IN_PROCESS -> OrderStatus.IN_PROCESS
    GsStStatus.SUCCESS -> OrderStatus.SUCCESS
    GsStStatus.ERROR -> OrderStatus.ERROR
    GsStStatus.NONE -> null
}

private fun GsStGasType.toTransportOrder(): GasType? = when (this) {
    GsStGasType.AI_92 -> GasType.AI_92
    GsStGasType.AI_95 -> GasType.AI_95
    GsStGasType.AI_98 -> GasType.AI_98
    GsStGasType.AI_100 -> GasType.AI_100
    GsStGasType.DIESEL -> GasType.DIESEL
    GsStGasType.NONE -> null
}

private fun List<GsStError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun GsStError.toTransportAd() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun GsStState.toResult(): ResponseResult? = when (this) {
    GsStState.RUNNING, GsStState.FINISHING -> ResponseResult.SUCCESS
    GsStState.FAILING -> ResponseResult.ERROR
    GsStState.NONE -> null
}
