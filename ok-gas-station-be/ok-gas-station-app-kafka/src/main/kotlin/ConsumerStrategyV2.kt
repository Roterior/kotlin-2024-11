package ru.otus.otuskotlin.gasstation.app.kafka

import ru.otus.otuskotlin.gasstation.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.gasstation.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.gasstation.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.gasstation.api.v2.mappers.toTransportOrder
import ru.otus.otuskotlin.gasstation.api.v2.models.IRequest
import ru.otus.otuskotlin.gasstation.api.v2.models.IResponse
import ru.otus.otuskotlin.gasstation.common.GsStContext

class ConsumerStrategyV2 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV2, config.kafkaTopicOutV2)
    }

    override fun serialize(source: GsStContext): String {
        val response: IResponse = source.toTransportOrder()
        return apiV2ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: GsStContext) {
        val request: IRequest = apiV2RequestDeserialize(value)
        target.fromTransport(request)
    }
}
