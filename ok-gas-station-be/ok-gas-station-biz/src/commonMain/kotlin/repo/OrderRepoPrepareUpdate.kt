package ru.otus.otuskotlin.gasstation.biz.repo

import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.common.models.GsStState
import ru.otus.otuskotlin.gasstation.cor.ICorChainDsl
import ru.otus.otuskotlin.gasstation.cor.worker

fun ICorChainDsl<GsStContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == GsStState.RUNNING }
    handle {
        orderRepoPrepare = orderRepoRead.copy().apply {
            gasType = orderValidated.gasType
            status = orderValidated.status
            price = orderValidated.price
            quantity = orderValidated.quantity
            summaryPrice = orderValidated.summaryPrice
            lock = orderValidated.lock
        }
    }
}
