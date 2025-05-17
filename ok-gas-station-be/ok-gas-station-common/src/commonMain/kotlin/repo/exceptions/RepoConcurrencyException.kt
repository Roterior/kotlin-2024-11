package ru.otus.otuskotlin.gasstation.common.repo.exceptions

import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId
import ru.otus.otuskotlin.gasstation.common.models.GsStOrderLock

class RepoConcurrencyException(id: GsStOrderId, expectedLock: GsStOrderLock, actualLock: GsStOrderLock?): RepoOrderException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
