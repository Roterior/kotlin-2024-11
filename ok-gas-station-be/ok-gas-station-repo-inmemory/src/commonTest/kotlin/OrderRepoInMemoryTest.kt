import ru.otus.otuskotlin.gasstation.backend.repo.tests.*
import ru.otus.otuskotlin.gasstation.repo.common.OrderRepoInitialized
import ru.otus.otuskotlin.gasstation.repo.inmemory.OrderRepoInMemory

class OrderRepoInMemoryCreateTest : RepoOrderCreateTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}

class OrderRepoInMemoryDeleteTest : RepoOrderDeleteTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderRepoInMemoryReadTest : RepoOrderReadTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderRepoInMemorySearchTest : RepoOrderSearchTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(),
        initObjects = initObjects,
    )
}

class OrderRepoInMemoryUpdateTest : RepoOrderUpdateTest() {
    override val repo = OrderRepoInitialized(
        OrderRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
