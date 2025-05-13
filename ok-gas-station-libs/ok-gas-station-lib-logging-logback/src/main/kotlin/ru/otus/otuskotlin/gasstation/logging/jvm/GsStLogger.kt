package ru.otus.otuskotlin.gasstation.logging.jvm

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.gasstation.logging.common.IGsStLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal GsStLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun gsStLoggerLogback(logger: Logger): IGsStLogWrapper = GsStLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun gsStLoggerLogback(clazz: KClass<*>): IGsStLogWrapper = gsStLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun gsStLoggerLogback(loggerId: String): IGsStLogWrapper = gsStLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
