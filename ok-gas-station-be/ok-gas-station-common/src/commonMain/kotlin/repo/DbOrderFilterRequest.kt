package ru.otus.otuskotlin.gasstation.common.repo

import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus

data class DbOrderFilterRequest(
    val titleFilter: String = "",
    val status: GsStStatus = GsStStatus.NONE,
    val gasType: GsStGasType = GsStGasType.NONE,
)
