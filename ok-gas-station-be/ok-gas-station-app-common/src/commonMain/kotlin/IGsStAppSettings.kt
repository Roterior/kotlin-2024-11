package ru.otus.otuskotlin.gasstation.app.common

import ru.otus.otuskotlin.gasstation.biz.GsStOrderProcessor
import ru.otus.otuskotlin.gasstation.common.GsStCorSettings

interface IGsStAppSettings {
    val processor: GsStOrderProcessor
    val corSettings: GsStCorSettings
}
