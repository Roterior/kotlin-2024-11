package ru.otus.otuskotlin.gasstation.repo.common

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class OrderRepoInitialized(
    val repo: IRepoOrderInitializable,
    initObjects: Collection<GsStOrder> = emptyList(),
) : IRepoOrderInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<GsStOrder> = save(initObjects).toList()
}
