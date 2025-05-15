package ru.otus.otuskotlin.gasstation.common.ws

interface IGsStWsSessionRepo {
    fun add(session: IGsStWsSession)
    fun clearAll()
    fun remove(session: IGsStWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IGsStWsSessionRepo {
            override fun add(session: IGsStWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IGsStWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}
