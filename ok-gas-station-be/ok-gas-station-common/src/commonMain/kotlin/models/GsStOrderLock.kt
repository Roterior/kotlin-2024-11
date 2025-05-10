package ru.otus.otuskotlin.gasstation.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class GsStOrderLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = GsStOrderLock("")
    }
}
