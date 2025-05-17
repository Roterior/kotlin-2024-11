package ru.otus.otuskotlin.gasstation.biz

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub
import ru.otus.otuskotlin.gasstation.biz.general.initStatus
import ru.otus.otuskotlin.gasstation.biz.general.operation
import ru.otus.otuskotlin.gasstation.biz.general.stubs
import ru.otus.otuskotlin.gasstation.biz.stubs.stubCreateSuccess
import ru.otus.otuskotlin.gasstation.biz.stubs.stubDbError
import ru.otus.otuskotlin.gasstation.biz.stubs.stubDeleteSuccess
import ru.otus.otuskotlin.gasstation.biz.stubs.stubNoCase
import ru.otus.otuskotlin.gasstation.biz.stubs.stubReadSuccess
import ru.otus.otuskotlin.gasstation.biz.stubs.stubSearchSuccess
import ru.otus.otuskotlin.gasstation.biz.stubs.stubUpdateSuccess
import ru.otus.otuskotlin.gasstation.biz.stubs.stubValidationBadId
import ru.otus.otuskotlin.gasstation.biz.validation.finishOrderFilterValidation
import ru.otus.otuskotlin.gasstation.biz.validation.finishOrderValidation
import ru.otus.otuskotlin.gasstation.biz.validation.validateIdNotEmpty
import ru.otus.otuskotlin.gasstation.biz.validation.validateIdProperFormat
import ru.otus.otuskotlin.gasstation.biz.validation.validateLockNotEmpty
import ru.otus.otuskotlin.gasstation.biz.validation.validateLockProperFormat
import ru.otus.otuskotlin.gasstation.biz.validation.validateSearchStringLength
import ru.otus.otuskotlin.gasstation.biz.validation.validation
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.cor.rootChain
import ru.otus.otuskotlin.gasstation.cor.worker

class GsStOrderProcessor(val corSettings: GsStCorSettings = GsStCorSettings.NONE) {

    suspend fun exec(ctx: GsStContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<GsStContext> {
        initStatus("Инициализация статуса")

        operation("Создание заказа", GsStCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderValidating") { orderValidating = orderRequest.copy() }
                worker("Очистка id") { orderValidating.id = GsStOrderId.NONE }

                finishOrderValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Получить заказ", GsStCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderValidating") { orderValidating = orderRequest.copy() }
                worker("Очистка id") { orderValidating.id = GsStOrderId(orderValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishOrderValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Изменить заказ", GsStCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderValidating") { orderValidating = orderRequest.copy() }
                worker("Очистка id") { orderValidating.id = GsStOrderId(orderValidating.id.asString().trim()) }
                worker("Очистка lock") { orderValidating.lock = GsStOrderLock(orderValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")

                finishOrderValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Удалить заказ", GsStCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderValidating") {
                    orderValidating = orderRequest.copy()
                }
                worker("Очистка id") { orderValidating.id = GsStOrderId(orderValidating.id.asString().trim()) }
                worker("Очистка lock") { orderValidating.lock = GsStOrderLock(orderValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")

                finishOrderValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск заказов", GsStCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в orderFilterValidating") { orderFilterValidating = orderFilterRequest.copy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishOrderFilterValidation("Успешное завершение процедуры валидации")
            }
        }
    }.build()
}
