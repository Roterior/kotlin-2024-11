package ru.otus.otuskotlin.gasstation.backend.repo.tests

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStError
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderIdRequest
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseErr
import ru.otus.otuskotlin.gasstation.common.repo.DbOrderResponseOk
import ru.otus.otuskotlin.gasstation.repo.common.OrderRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoOrderReadTest {
    abstract val repo: OrderRepoInitialized
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readOrder(DbOrderIdRequest(readSucc.id))

        assertIs<DbOrderResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readOrder(DbOrderIdRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbOrderResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: GsStError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitOrders("read") {
        override val initObjects: List<GsStOrder> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = GsStOrderId("order-repo-read-notFound")

    }
}
