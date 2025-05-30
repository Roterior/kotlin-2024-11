package ru.otus.otuskotlin.gasstation.biz.exceptions.exceptions

import ru.otus.otuskotlin.gasstation.common.models.GsStWorkMode

class GsStOrderDbNotConfiguredException(val workMode: GsStWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
