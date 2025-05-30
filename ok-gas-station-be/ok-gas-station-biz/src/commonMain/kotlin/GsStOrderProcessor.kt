package ru.otus.otuskotlin.gasstation.biz

import ru.otus.otuskotlin.gasstation.biz.general.initStatus
import ru.otus.otuskotlin.gasstation.biz.general.operation
import ru.otus.otuskotlin.gasstation.biz.general.stubs
import ru.otus.otuskotlin.gasstation.biz.repo.checkLock
import ru.otus.otuskotlin.gasstation.biz.repo.initRepo
import ru.otus.otuskotlin.gasstation.biz.repo.prepareResult
import ru.otus.otuskotlin.gasstation.biz.repo.repoCreate
import ru.otus.otuskotlin.gasstation.biz.repo.repoDelete
import ru.otus.otuskotlin.gasstation.biz.repo.repoPrepareCreate
import ru.otus.otuskotlin.gasstation.biz.repo.repoPrepareDelete
import ru.otus.otuskotlin.gasstation.biz.repo.repoPrepareUpdate
import ru.otus.otuskotlin.gasstation.biz.repo.repoRead
import ru.otus.otuskotlin.gasstation.biz.repo.repoSearch
import ru.otus.otuskotlin.gasstation.biz.repo.repoUpdate
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
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.cor.chain
import ru.otus.otuskotlin.gasstation.cor.rootChain
import ru.otus.otuskotlin.gasstation.cor.worker

class GsStOrderProcessor(val corSettings: GsStCorSettings = GsStCorSettings.NONE) {

    suspend fun exec(ctx: GsStContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<GsStContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

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
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание заказа в БД")
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика чтения"
                repoRead("Чтение заказа из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == GsStState.RUNNING }
                    handle { orderRepoDone = orderRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика сохранения"
                repoRead("Чтение заказа из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для заказа")
                repoUpdate("Обновление заказа в БД")
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика удаления"
                repoRead("Чтение заказа из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление заказа из БД")
            }
            prepareResult("Подготовка ответа")
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
            repoSearch("Поиск заказа в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}
