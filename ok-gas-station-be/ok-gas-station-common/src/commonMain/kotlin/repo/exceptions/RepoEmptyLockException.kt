package ru.otus.otuskotlin.gasstation.common.repo.exceptions

import ru.otus.otuskotlin.gasstation.common.models.GsStOrderId

class RepoEmptyLockException(id: GsStOrderId): RepoOrderException(
    id,
    "Lock is empty in DB"
)
