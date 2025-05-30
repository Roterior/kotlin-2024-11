package ru.otus.otuskotlin.gasstation.biz.repo

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseErrWithData
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == GsStState.RUNNING }
    handle {
        val request = DbOrderRequest(orderRepoPrepare)
        when (val result = orderRepo.updateOrder(request)) {
            is DbOrderResponseOk -> orderRepoDone = result.data
            is DbOrderResponseErr -> fail(result.errors)
            is DbOrderResponseErrWithData -> {
                fail(result.errors)
                orderRepoDone = result.data
            }
        }
    }
}
