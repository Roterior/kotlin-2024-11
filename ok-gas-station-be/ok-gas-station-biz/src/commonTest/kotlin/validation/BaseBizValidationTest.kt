package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand

abstract class BaseBizValidationTest {
    protected abstract val command: GsStCommand
    private val settings by lazy { GsStCorSettings() }
    protected val processor by lazy { GsStOrderProcessor(settings) }
}
