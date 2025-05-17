package ru.otus.otuskotlin.gasstation.backend.repository.inmemory

import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderFilterRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderIdRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.gasstation.common.repo.DbOrdersResponseOk
import ru.otus.otuskotlin.gasstation.common.repo.IDbOrderResponse
import ru.otus.otuskotlin.gasstation.common.repo.IDbOrdersResponse
import ru.otus.otuskotlin.gasstation.common.repo.IRepoOrder
import ru.otus.otuskotlin.gasstation.stubs.GsStOrderStub

class OrderRepoStub() : IRepoOrder {
    override suspend fun createOrder(rq: DbOrderRequest): IDbOrderResponse {
        return DbOrderResponseOk(
            data = GsStOrderStub.get(),
        )
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): IDbOrderResponse {
        return DbOrderResponseOk(
            data = GsStOrderStub.get(),
        )
    }

    override suspend fun updateOrder(rq: DbOrderRequest): IDbOrderResponse {
        return DbOrderResponseOk(
            data = GsStOrderStub.get(),
        )
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): IDbOrderResponse {
        return DbOrderResponseOk(
            data = GsStOrderStub.get(),
        )
    }

    override suspend fun searchOrder(rq: DbOrderFilterRequest): IDbOrdersResponse {
        return DbOrdersResponseOk(
            data = GsStOrderStub.prepareSearchList(filter = "", GsStGasType.AI_92),
        )
    }
}
