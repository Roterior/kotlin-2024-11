package ru.otus.otuskotlin.gasstation.backend.repo.tests

import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.repo.*
import ru.otus.otuskotlin.gasstation.repo.common.OrderRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoOrderDeleteTest {

    abstract val repo: OrderRepoInitialized
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = GsStOrderId("order-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteOrder(DbOrderIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbOrderResponseOk>(result)
        assertEquals(deleteSucc.status, result.data.status)
        assertEquals(deleteSucc.gasType, result.data.gasType)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readOrder(DbOrderIdRequest(notFoundId, lock = lockOld))

        assertIs<DbOrderResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteOrder(DbOrderIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbOrderResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitOrders("delete") {
        override val initObjects: List<GsStOrder> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
