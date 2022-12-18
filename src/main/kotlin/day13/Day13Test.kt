package day13

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Test() {
    @Test
    fun intInt() {
        assertEquals(0, comparePair("12", "12"))
        assertEquals(-1, comparePair("7", "8"))
        assertEquals(1, comparePair("4", "3"))
    }

    @Test
    fun listList() {
        assertEquals(0, comparePair("[1,2,3]", "[1,2,3]"))
        assertEquals(-1, comparePair("[]", "[1]"))
        assertEquals(-1, comparePair("[4,8]", "[4,8,12]"))
        assertEquals(-1, comparePair("[4,7]", "[4,8,12]"))
        assertEquals(1, comparePair("[34]", "[]"))
        assertEquals(1, comparePair("[4,7,13]", "[4,7]"))
        assertEquals(1, comparePair("[4,7,13]", "[4,7,12]"))
    }

    @Test
    fun intList() {
        assertEquals(0, comparePair("11", "[11]"))
        assertEquals(0, comparePair("[33]", "33"))
        assertEquals(-1, comparePair("123", "[123, 234]"))
        assertEquals(1, comparePair("234", "[123, 234]"))
    }
}
