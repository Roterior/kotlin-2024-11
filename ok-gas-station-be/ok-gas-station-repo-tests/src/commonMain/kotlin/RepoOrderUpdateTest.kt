package ru.otus.otuskotlin.gasstation.backend.repo.tests

import ru.otus.otuskotlin.gasstation.common.models.*
import ru.otus.otuskotlin.gasstation.common.repo.*
import ru.otus.otuskotlin.gasstation.repo.common.OrderRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoOrderUpdateTest {
    abstract val repo: OrderRepoInitialized
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = GsStOrderId("order-repo-update-not-found")
    protected val lockBad = GsStOrderLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = GsStOrderLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        GsStOrder(
            id = updateSucc.id,
            status = GsStStatus.CREATED,
            gasType = GsStGasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f,
            lock = initObjects.first().lock
        )
    }
    private val reqUpdateNotFound = GsStOrder(
        id = updateIdNotFound,
        status = GsStStatus.CREATED,
        gasType = GsStGasType.AI_92,
        price = 10f,
        quantity = 2f,
        summaryPrice = 20f,
        lock = initObjects.first().lock
    )
    private val reqUpdateConc by lazy {
        GsStOrder(
            id = updateConc.id,
            status = GsStStatus.CREATED,
            gasType = GsStGasType.AI_92,
            price = 10f,
            quantity = 2f,
            summaryPrice = 20f,
            lock = lockBad
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateOrder(DbOrderRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbOrderResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbOrderResponseErrWithData)?.errors}")
        assertIs<DbOrderResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.status, result.data.status)
        assertEquals(reqUpdateSucc.gasType, result.data.gasType)
        assertEquals(reqUpdateSucc.price, result.data.price)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateOrder(DbOrderRequest(reqUpdateNotFound))
        assertIs<DbOrderResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateOrder(DbOrderRequest(reqUpdateConc))
        assertIs<DbOrderResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitOrders("update") {
        override val initObjects: List<GsStOrder> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
