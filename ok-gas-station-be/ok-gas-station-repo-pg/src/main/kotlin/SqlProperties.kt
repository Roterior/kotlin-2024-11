package ru.otus.otuskotlin.gasstation.backend.repo.postgresql

data class SqlProperties(
    val host: String = "postgres",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "gasstation-pass",
    val database: String = "gasstation_orders",
    val schema: String = "public",
    val table: String = "orders",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
