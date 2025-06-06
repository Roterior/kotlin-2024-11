package ru.otus.otuskotlin.gasstation.logging

import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.gasstation.logging.jvm.gsStLoggerLogback
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertTrue

class LoggerTest {
    private val logId = "test-logger"
    val lggr = LoggerFactory.getLogger("xx")
    data class Xx(val x: String = "sdf")


    @Test
    fun slf4jTest() {
        @Suppress("LoggingPlaceholderCountMatchesArgumentCount")
        lggr.info("ggg {} {} {}", 1, "sdf", Xx(), Xx("234234"))
        // ------------это objs - !    !     !
        // -----------------------------------------это data- !
    }

    @Test
    fun `logger init`() {
        val output = invokeLogger {
            println("Some action")
        }

        assertTrue(Regex("Started .* $logId.*").containsMatchIn(output.toString()))
        assertTrue(output.toString().contains("Some action"))
        assertTrue(Regex("Finished .* $logId.*").containsMatchIn(output.toString()))
    }

    @Test
    fun `logger fails`() {
        val output = invokeLogger {
            throw RuntimeException("Some action")
        }

        assertTrue(Regex("Started .* $logId.*").containsMatchIn(output.toString()))
        assertTrue(Regex("Failed .* $logId.*").containsMatchIn(output.toString()))
    }

    private fun invokeLogger(block: suspend () -> Unit): ByteArrayOutputStream {
        val outputStreamCaptor = outputStreamCaptor()

        try {
            runBlocking {
                val logger = gsStLoggerLogback(this::class)
                logger.doWithLogging(logId, block = block)
            }
        } catch (ignore: RuntimeException) {
        }

        return outputStreamCaptor
    }

    private fun outputStreamCaptor(): ByteArrayOutputStream {
        return ByteArrayOutputStream().apply {
            System.setOut(PrintStream(this))
        }
    }
}
