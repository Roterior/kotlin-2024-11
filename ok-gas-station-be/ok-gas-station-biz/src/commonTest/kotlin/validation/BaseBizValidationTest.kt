package ru.otus.otuskotlin.gasstation.biz.validation

import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings
import ru.otus.otuskotlin.gasstation.common.models.GsStCommand
import ru.otus.otuskotlin.gasstation.repo.common.OrderRepoInitialized
import ru.otus.otuskotlin.gasstation.repo.inmemory.OrderRepoInMemory
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub

abstract class BaseBizValidationTest {
    protected abstract val command: GsStCommand
    private val repo = OrderRepoInitialized(
        repo = OrderRepoInMemory(),
        initObjects = listOf(
            GsStOrderStub.get(),
        ),
    )
    private val settings by lazy { GsStCorSettings(repoTest = repo) }
    protected val processor by lazy { GsStOrderProcessor(settings) }
}
