package ru.otus.otuskotlin.gasstation.common.repo

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStError

sealed interface IDbOrderResponse: IDbResponse<GsStOrder>

data class DbOrderResponseOk(
    val data: GsStOrder
): IDbOrderResponse

data class DbOrderResponseErr(
    val errors: List<GsStError> = emptyList()
): IDbOrderResponse {
    constructor(err: GsStError): this(listOf(err))
}

data class DbOrderResponseErrWithData(
    val data: GsStOrder,
    val errors: List<GsStError> = emptyList()
): IDbOrderResponse {
    constructor(order: GsStOrder, err: GsStError): this(order, listOf(err))
}
