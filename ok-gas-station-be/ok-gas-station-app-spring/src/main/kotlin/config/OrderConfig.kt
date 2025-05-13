package ru.otus.otuskotlin.gasstation.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.logging.common.GsStLoggerProvider
import ru.otus.otuskotlin.gasstation.logging.jvm.gsStLoggerLogback

@Suppress("unused")
@Configuration
class OrderConfig {
    @Bean
    fun processor(corSettings: GsStCorSettings) = GsStOrderProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): GsStLoggerProvider = GsStLoggerProvider { gsStLoggerLogback(it) }

    @Bean
    fun corSettings(): GsStCorSettings = GsStCorSettings(
        loggerProvider = loggerProvider(),
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
