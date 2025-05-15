package ru.otus.otuskotlin.gasstation.app.spring.config

import ru.otus.otuskotlin.gasstation.app.common.IGsStAppSettings
import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings

data class GsStAppSettings(
    override val corSettings: GsStCorSettings,
    override val processor: GsStOrderProcessor,
): IGsStAppSettings
