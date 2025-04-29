package ru.otus.otuskotlin.gasstation.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderFilter
import ru.otus.otuskotlin.gasstation.common.models.GsStRequestId
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode
import ru.otus.otuskotlin.gasstation.common.stubs.GsStStubs

data class GsStContext(
    var command: GsStCommand = GsStCommand.NONE,
    var state: GsStState = GsStState.NONE,
    val errors: MutableList<GsStError> = mutableListOf(),

    var workMode: GsStWorkMode = GsStWorkMode.PROD,
    var stubCase: GsStStubs = GsStStubs.NONE,

    var requestId: GsStRequestId = GsStRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var orderRequest: GsStOrder = GsStOrder(),
    var orderFilterRequest: GsStOrderFilter = GsStOrderFilter(),

    var orderResponse: GsStOrder = GsStOrder(),
    var ordersResponse: MutableList<GsStOrder> = mutableListOf()
)
