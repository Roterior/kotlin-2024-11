package ru.otus.otuskotlin.gasstation.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType

fun Table.gasTypeEnum(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.GAS_TYPE_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.GAS_TYPE_AI_92 -> GsStGasType.AI_92
            SqlFields.GAS_TYPE_AI_95 -> GsStGasType.AI_95
            SqlFields.GAS_TYPE_AI_98 -> GsStGasType.AI_98
            SqlFields.GAS_TYPE_AI_100 -> GsStGasType.AI_100
            SqlFields.GAS_TYPE_DIESEL -> GsStGasType.DIESEL
            else -> GsStGasType.NONE
        }
    },
    toDb = { value ->
        when (value) {
            GsStGasType.AI_92 -> PgGasTypeAi92
            GsStGasType.AI_95 -> PgGasTypeAi95
            GsStGasType.AI_98 -> PgGasTypeAi98
            GsStGasType.AI_100 -> PgGasTypeAi100
            GsStGasType.DIESEL -> PgGasTypeDiesel
            GsStGasType.NONE -> throw Exception("Wrong value of Gas Type. NONE is unsupported")
        }
    }
)

sealed class PgGasTypeValue(enVal: String) : PGobject() {
    init {
        type = SqlFields.GAS_TYPE_TYPE
        value = enVal
    }
}

object PgGasTypeAi92 : PgGasTypeValue(SqlFields.GAS_TYPE_AI_92) {
    private fun readResolve(): Any = PgGasTypeAi92
}

object PgGasTypeAi95 : PgGasTypeValue(SqlFields.GAS_TYPE_AI_95) {
    private fun readResolve(): Any = PgGasTypeAi95
}

object PgGasTypeAi98 : PgGasTypeValue(SqlFields.GAS_TYPE_AI_98) {
    private fun readResolve(): Any = PgGasTypeAi98
}

object PgGasTypeAi100 : PgGasTypeValue(SqlFields.GAS_TYPE_AI_100) {
    private fun readResolve(): Any = PgGasTypeAi100
}

object PgGasTypeDiesel : PgGasTypeValue(SqlFields.GAS_TYPE_DIESEL) {
    private fun readResolve(): Any = PgGasTypeDiesel
}
