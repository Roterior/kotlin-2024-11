package ru.otus.otuskotlin.gasstation.biz.repo

import ru.otus.otuskotlin.gasstation.biz.exceptions.exceptions.GsStOrderDbNotConfiguredException
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.errorSystem
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode
import ru.otus.otuskotlin.gasstation.common.repo.IRepoOrder
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        orderRepo = when {
            workMode == GsStWorkMode.TEST -> corSettings.repoTest
            workMode == GsStWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != GsStWorkMode.STUB && orderRepo == IRepoOrder.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = GsStOrderDbNotConfiguredException(workMode)
            )
        )
    }
}
