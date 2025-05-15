package ru.otus.otuskotlin.gasstation.app.kafka

import ru.otus.otuskotlin.gasstation.api.v1.apiV1RequestDeserialize
import ru.otus.otuskotlin.gasstation.api.v1.apiV1ResponseSerialize
import ru.otus.otuskotlin.gasstation.api.v1.models.IRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.IResponse
import ru.otus.otuskotlin.gasstation.common.GsStContext
import ru.otus.otuskotlin.gasstation.mappers.v1.fromTransport
import ru.otus.otuskotlin.gasstation.mappers.v1.toTransportOrder

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: GsStContext): String {
        val response: IResponse = source.toTransportOrder()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: GsStContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
