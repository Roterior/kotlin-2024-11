package ru.otus.otuskotlin.gasstation.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.gasstation.api.log1.mapper.toLog
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.asGsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import kotlin.reflect.KClass

suspend inline fun <T> IGsStAppSettings.controllerHelper(
    crossinline getRequest: suspend GsStContext.() -> Unit,
    crossinline toResponse: suspend GsStContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = GsStContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = GsStState.FAILING
        ctx.errors.add(e.asGsStError())
        processor.exec(ctx)
        if (ctx.command == GsStCommand.NONE) {
            ctx.command = GsStCommand.READ
        }
        ctx.toResponse()
    }
}
