import kotlin.test.Test
import kotlin.test.assertIs

class TypesTest {

    @Test
    fun types() {
        val b: Byte = 1
        assertIs<Byte>(b)
        assertIs<Int>(1)

        val b2: Byte = (1 + 2).toByte()
        assertIs<Byte>(b2)
        assertIs<Int>(1 + 2)

        val b3 = 2.toByte()
        assertIs<Byte>(b3)

        // val ub: UByte = 1 // ERROR Initializer type mismatch
        val ub2: UByte = 1U
        assertIs<UByte>(ub2)

        val l = 1L
        assertIs<Long>(l)

        val f = 1.2f
        assertIs<Float>(f)
        // val f2 : Float = 1.2 // ERROR Initializer type mismatch

        val d = 1.2
        assertIs<Double>(d)
    }

    @Test
    fun types2() {
        val a = 1
        // val b: Long = a // ERROR Initializer type mismatch
        val b: Long = a.toLong()
        assertIs<Long>(b)

        val f: Float = 1.0f
        assertIs<Float>(f)

        val d = a * 1.0
        assertIs<Double>(d)

        val l = a + 2L
        assertIs<Long>(l)

        // val ui = a + 2U // ERROR
        val ui2 = a.toUInt() + 2U
        assertIs<UInt>(ui2)
    }
}
