package ru.otus.otuskotlin.gasstation.biz.repo

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrdersResponseErr
import ru.otus.otuskotlin.gasstation.common.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск заказа в БД по фильтру"
    on { state == GsStState.RUNNING }
    handle {
        val request = DbOrderFilterRequest(
            titleFilter = orderFilterValidated.searchString,
            status = orderFilterValidated.status,
            gasType = orderFilterValidated.gasType
        )
        when (val result = orderRepo.searchOrder(request)) {
            is DbOrdersResponseOk -> ordersRepoDone = result.data.toMutableList()
            is DbOrdersResponseErr -> fail(result.errors)
        }
    }
}
