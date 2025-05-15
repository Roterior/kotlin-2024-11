package ru.otus.otuskotlin.gasstation.api.log1.mapper

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.gasstation.api.log1.models.*
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.*

fun GsStContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-gasstation",
    order = toGsStLog(),
    errors = errors.map { it.toLog() },
)

private fun GsStContext.toGsStLog(): GsStLogModel? {
    val orderNone = GsStOrder()
    return GsStLogModel(
        requestId = requestId.takeIf { it != GsStRequestId.NONE }?.asString(),
        requestOrder = orderRequest.takeIf { it != orderNone }?.toLog(),
        responseOrder = orderResponse.takeIf { it != orderNone }?.toLog(),
        responseOrders = ordersResponse.takeIf { it.isNotEmpty() }?.filter { it != orderNone }?.map { it.toLog() },
        requestFilter = orderFilterRequest.takeIf { it != GsStOrderFilter() }?.toLog(),
    ).takeIf { it != GsStLogModel() }
}

private fun GsStOrderFilter.toLog() = OrderFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    status = status.takeIf { it != GsStStatus.NONE }?.name,
    gasType = gasType.takeIf { it != GsStGasType.NONE }?.name
)

private fun GsStError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
    exception = exception?.let{ "${it.message}\n${it.stackTraceToString()}"}
)

private fun GsStOrder.toLog() = OrderLog(
    id = id.takeIf { it != GsStOrderId.NONE }?.asString(),
    status = status.takeIf { it != GsStStatus.NONE }?.name,
    gasType = gasType.takeIf { it != GsStGasType.NONE }?.name,
    price = price.takeIf { it != 0f }?.toString(),
    quantity = quantity.takeIf { it != 0f }?.toString(),
    summaryPrice = summaryPrice.takeIf { it != 0f }?.toString()
)
