package ru.otus.otuskotlin.gasstation.app.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.otus.otuskotlin.gasstation.backend.repo.postgresql.SqlProperties

@ConfigurationProperties(prefix = "psql")
data class OrderConfigPostgres(
    var host: String = "postgres",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "gasstation-pass",
    var database: String = "gasstation_orders",
    var schema: String = "public",
    var table: String = "orders",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}