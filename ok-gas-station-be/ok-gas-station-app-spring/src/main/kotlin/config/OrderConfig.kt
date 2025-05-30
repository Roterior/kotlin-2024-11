package ru.otus.otuskotlin.gasstation.app.spring.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.gasstation.backend.repo.postgresql.RepoOrderSql
import ru.otus.otuskotlin.gasstation.backend.repository.inmemory.OrderRepoStub
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.common.repo.IRepoOrder
import ru.otus.otuskotlin.gasstation.logging.common.GsStLoggerProvider
import ru.otus.otuskotlin.gasstation.logging.jvm.gsStLoggerLogback
import ru.otus.otuskotlin.gasstation.repo.inmemory.OrderRepoInMemory

@Suppress("unused")
@EnableConfigurationProperties(OrderConfigPostgres::class)
@Configuration
class OrderConfig(val postgresConfig: OrderConfigPostgres) {

    val logger: Logger = LoggerFactory.getLogger(OrderConfig::class.java)

    @Bean
    fun processor(corSettings: GsStCorSettings) = GsStOrderProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): GsStLoggerProvider = GsStLoggerProvider { gsStLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoOrder = OrderRepoInMemory()

    @Bean
    fun prodRepo(): IRepoOrder = RepoOrderSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with ${this}")
    }

    @Bean
    fun stubRepo(): IRepoOrder = OrderRepoStub()

    @Bean
    fun corSettings(): GsStCorSettings = GsStCorSettings(
        loggerProvider = loggerProvider(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo()
    )

    @Bean
    fun appSettings(
        corSettings: GsStCorSettings,
        processor: GsStOrderProcessor,
    ) = GsStAppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
