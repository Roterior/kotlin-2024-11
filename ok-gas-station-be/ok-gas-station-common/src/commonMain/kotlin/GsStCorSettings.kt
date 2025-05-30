package ru.otus.otuskotlin.gasstation.common

import ru.otus.otuskotlin.gasstation.common.repo.IRepoOrder
import ru.otus.otuskotlin.gasstation.common.ws.IGsStWsSessionRepo
import ru.otus.otuskotlin.gasstation.logging.common.GsStLoggerProvider

data class GsStCorSettings(
    val loggerProvider: GsStLoggerProvider = GsStLoggerProvider(),
    val wsSessions: IGsStWsSessionRepo = IGsStWsSessionRepo.NONE,
    val repoStub: IRepoOrder = IRepoOrder.NONE,
    val repoTest: IRepoOrder = IRepoOrder.NONE,
    val repoProd: IRepoOrder = IRepoOrder.NONE,

) {
    companion object {
        val NONE = GsStCorSettings()
    }
}
