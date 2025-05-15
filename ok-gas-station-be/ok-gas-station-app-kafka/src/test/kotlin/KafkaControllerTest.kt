package ru.otus.otuskotlin.gasstation.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.gasstation.api.v1.apiV1RequestSerialize
import ru.otus.otuskotlin.gasstation.api.v1.apiV1ResponseDeserialize
import ru.otus.otuskotlin.gasstation.api.v1.models.GasType
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateObject
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateRequest
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderCreateResponse
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderDebug
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderRequestDebugMode
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderRequestDebugStubs
import ru.otus.otuskotlin.gasstation.api.v1.models.OrderStatus
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {

    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        OrderCreateRequest(
                            order = OrderCreateObject(
                                status = OrderStatus.CREATED,
                                gasType = GasType.AI_92,
                                price = 10f,
                                quantity = 2f,
                                summaryPrice = 20f
                            ),
                            debug = OrderDebug(
                                mode = OrderRequestDebugMode.STUB,
                                stub = OrderRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<OrderCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals(OrderStatus.CREATED, result.order?.status)
    }

    companion object {
        const val PARTITION = 0
    }
}
