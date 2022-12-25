package day25

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day25Test {
    @Test
    fun toDecimal() {
        assertEquals("2", decimalToSnafu(2))
        assertEquals("=", decimalToSnafu(-2))
        assertEquals("1=", decimalToSnafu(3))
        assertEquals("12", decimalToSnafu(7))
        assertEquals("2=", decimalToSnafu(8))
        assertEquals("2-", decimalToSnafu(9))
        assertEquals("20", decimalToSnafu(10))
        assertEquals("21", decimalToSnafu(11))
        assertEquals("22", decimalToSnafu(12))
        assertEquals("1==", decimalToSnafu(13))
        assertEquals("-2", decimalToSnafu(-3))
        assertEquals("=0", decimalToSnafu(-10))
        assertEquals("==", decimalToSnafu(-12))
        assertEquals("-22", decimalToSnafu(-13))
        assertEquals("2=-1=0", decimalToSnafu(4890))
        assertEquals("2011-=2=-1020-1===-1", decimalToSnafu(39021690220321))
    }
}
