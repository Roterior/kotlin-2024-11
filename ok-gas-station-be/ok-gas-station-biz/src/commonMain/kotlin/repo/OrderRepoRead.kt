package ru.otus.otuskotlin.gasstation.biz.repo

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.helpers.fail
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderIdRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseErrWithData
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение заказа из БД"
    on { state == GsStState.RUNNING }
    handle {
        val request = DbOrderIdRequest(orderValidated)
        when (val result = orderRepo.readOrder(request)) {
            is DbOrderResponseOk -> orderRepoRead = result.data
            is DbOrderResponseErr -> fail(result.errors)
            is DbOrderResponseErrWithData -> {
                fail(result.errors)
                orderRepoRead = result.data
            }
        }
    }
}
