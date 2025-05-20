package ru.otus.otuskotlin.gasstation.backend.repo.postgresql

object SqlFields {

    const val ID = "id"
    const val STATUS = "status"
    const val GAS_TYPE = "gas_type"
    const val PRICE = "price"
    const val QUANTITY = "quantity"
    const val SUMMARY_PRICE = "summary_price"

    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"

    const val STATUS_TYPE = "statuses_type"
    const val STATUS_CREATED = "created"
    const val STATUS_IN_PROCESS = "in_process"
    const val STATUS_SUCCESS = "success"
    const val STATUS_ERROR = "error"

    const val GAS_TYPE_TYPE = "gas_types_type"
    const val GAS_TYPE_AI_92 = "ai_92"
    const val GAS_TYPE_AI_95 = "ai_95"
    const val GAS_TYPE_AI_98 = "ai_98"
    const val GAS_TYPE_AI_100 = "ai_100"
    const val GAS_TYPE_DIESEL = "diesel"

    const val FILTER_STATUS = STATUS
    const val FILTER_GAS_TYPE = GAS_TYPE

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(ID, STATUS, GAS_TYPE, PRICE, QUANTITY, SUMMARY_PRICE, LOCK)
}
