package ru.otus.otuskotlin.gasstation.backend.repo.tests

import ru.otus.otuskotlin.gasstation.common.models.GsStGasType
import ru.otus.otuskotlin.gasstation.common.models.GsStOrder
import ru.otus.otuskotlin.gasstation.common.models.GsStStatus
import ru.otus.otuskotlin.gasstation.common.repo.*
import ru.otus.otuskotlin.gasstation.repo.common.OrderRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.fail

abstract class RepoOrderSearchTest {

    abstract val repo: OrderRepoInitialized

    protected open val initializedObjects: List<GsStOrder> = initObjects

    @Test
    fun searchStatus() = runRepoTest {
        val result = repo.searchOrder(DbOrderFilterRequest(status = searchStatus))
        if (result is DbOrdersResponseErr) {
            result.errors.forEach { it.exception?.printStackTrace() }
            fail()
        }
        assertIs<DbOrdersResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchGasType() = runRepoTest {
        val result = repo.searchOrder(DbOrderFilterRequest(gasType = GsStGasType.AI_95))
        if (result is DbOrdersResponseErr) {
            result.errors.forEach { it.exception?.printStackTrace() }
            fail()
        }
        assertIs<DbOrdersResponseOk>(result)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitOrders("search") {

        val searchStatus = GsStStatus.IN_PROCESS
        override val initObjects: List<GsStOrder> = listOf(
            createInitTestModel("order1", status = GsStStatus.CREATED, gasType = GsStGasType.AI_92),
            createInitTestModel("order2", status = searchStatus, gasType = GsStGasType.AI_92),
            createInitTestModel("order3", status = GsStStatus.CREATED, gasType = GsStGasType.AI_95),
            createInitTestModel("order4", status = searchStatus, gasType = GsStGasType.AI_92),
            createInitTestModel("order5", status = GsStStatus.CREATED, gasType = GsStGasType.AI_95),
        )
    }
}
