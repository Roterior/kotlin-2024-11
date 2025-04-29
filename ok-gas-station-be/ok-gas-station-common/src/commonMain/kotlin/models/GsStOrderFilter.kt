package ru.otus.otuskotlin.gasstation.common.models

data class GsStOrderFilter(
    var searchString: String = "",
    var status: GsStStatus = GsStStatus.NONE,
    var gasType: GsStGasType = GsStGasType.NONE,
)
