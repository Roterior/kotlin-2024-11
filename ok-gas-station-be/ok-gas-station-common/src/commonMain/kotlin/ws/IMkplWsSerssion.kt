package ru.otus.otuskotlin.gasstation.common.ws

interface IGsStWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : IGsStWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}
