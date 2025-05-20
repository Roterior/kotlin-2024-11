package ru.otus.otuskotlin.gasstation.repo.common

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.repo.IRepoOrder

interface IRepoOrderInitializable: IRepoOrder {
    fun save(orders: Collection<GsStOrder>) : Collection<GsStOrder>
}
