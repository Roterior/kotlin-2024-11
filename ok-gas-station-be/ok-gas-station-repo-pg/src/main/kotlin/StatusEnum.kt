package ru.otus.otuskotlin.gasstation.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus

fun Table.statusEnum(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.STATUS,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.STATUS_CREATED -> GsStStatus.CREATED
            SqlFields.STATUS_IN_PROCESS -> GsStStatus.IN_PROCESS
            SqlFields.STATUS_SUCCESS -> GsStStatus.SUCCESS
            SqlFields.STATUS_ERROR -> GsStStatus.ERROR
            else -> GsStStatus.NONE
        }
    },
    toDb = { value ->
        when (value) {
            GsStStatus.CREATED -> PgStatusCreated
            GsStStatus.IN_PROCESS -> PgStatusInProcess
            GsStStatus.SUCCESS -> PgStatusSuccess
            GsStStatus.ERROR -> PgStatusError
            GsStStatus.NONE -> throw Exception("Wrong value of Status. NONE is unsupported")
        }
    }
)

sealed class PgStatusValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.STATUS_TYPE
        value = eValue
    }
}

object PgStatusCreated : PgStatusValue(SqlFields.STATUS_CREATED) {
    private fun readResolve(): Any = PgStatusCreated
}

object PgStatusInProcess : PgStatusValue(SqlFields.STATUS_IN_PROCESS) {
    private fun readResolve(): Any = PgStatusInProcess
}

object PgStatusSuccess : PgStatusValue(SqlFields.STATUS_SUCCESS) {
    private fun readResolve(): Any = PgStatusSuccess
}

object PgStatusError : PgStatusValue(SqlFields.STATUS_ERROR) {
    private fun readResolve(): Any = PgStatusError
}
