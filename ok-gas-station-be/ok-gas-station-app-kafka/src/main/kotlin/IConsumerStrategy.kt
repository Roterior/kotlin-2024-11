package ru.otus.otuskotlin.gasstation.app.kafka

import ru.otus.otuskotlin.gasstation.common.GsStContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: GsStContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: GsStContext)
}
