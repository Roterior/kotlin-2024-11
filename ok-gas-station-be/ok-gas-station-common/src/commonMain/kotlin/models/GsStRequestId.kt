package ru.otus.otuskotlin.gasstation.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class GsStRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = GsStRequestId("")
    }
}
