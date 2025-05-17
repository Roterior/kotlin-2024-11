package ru.otus.otuskotlin.gasstation.common.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import ru.otus.otuskotlin.gasstation.common.helpers.errorSystem
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class OrderRepoBase: IRepoOrder {

    protected suspend fun tryOrderMethod(timeout: Duration = 10.seconds, ctx: CoroutineContext = Dispatchers.IO, block: suspend () -> IDbOrderResponse) = try {
        withTimeout(timeout) {
            withContext(ctx) {
                block()
            }
        }
    } catch (e: Throwable) {
        DbOrderResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryOrdersMethod(block: suspend () -> IDbOrdersResponse) = try {
        block()
    } catch (e: Throwable) {
        DbOrdersResponseErr(errorSystem("methodException", e = e))
    }

}
