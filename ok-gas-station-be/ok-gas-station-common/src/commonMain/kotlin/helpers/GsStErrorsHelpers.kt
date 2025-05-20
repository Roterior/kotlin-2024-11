package ru.otus.otuskotlin.gasstation.common.helpers

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStError
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.logging.common.LogLevel

fun Throwable.asGsStError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = GsStError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun GsStContext.addError(vararg error: GsStError) = errors.addAll(error)

inline fun GsStContext.addErrors(error: Collection<GsStError>) = errors.addAll(error)

inline fun GsStContext.fail(error: GsStError) {
    addError(error)
    state = GsStState.FAILING
}

inline fun GsStContext.fail(errors: Collection<GsStError>) {
    addErrors(errors)
    state = GsStState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = GsStError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = GsStError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
