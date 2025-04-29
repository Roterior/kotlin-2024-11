package ru.otus.otuskotlin.gasstation.common.models

data class GsStOrder(
    var id: GsStOrderId = GsStOrderId.NONE,
    var lock: GsStOrderLock = GsStOrderLock.NONE,

    var status: GsStStatus = GsStStatus.NONE,
    var gasType: GsStGasType = GsStGasType.NONE,
    var price: Float = 0f,
    var quantity: Float = 0f,
    var summaryPrice: Float = 0f
) {

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = GsStOrder()
    }
}
