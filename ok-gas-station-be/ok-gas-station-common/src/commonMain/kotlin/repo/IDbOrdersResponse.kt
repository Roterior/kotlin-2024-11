package ru.otus.otuskotlin.gasstation.common.repo

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStError

sealed interface IDbOrdersResponse: IDbResponse<List<GsStOrder>>

data class DbOrdersResponseOk(
    val data: List<GsStOrder>
): IDbOrdersResponse

@Suppress("unused")
data class DbOrdersResponseErr(
    val errors: List<GsStError> = emptyList()
): IDbOrdersResponse {
    constructor(err: GsStError): this(listOf(err))
}
