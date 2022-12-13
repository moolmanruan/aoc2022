import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class TestDay13() {
    @Test
    fun intInt() {
        assertEquals(0, comparePair("12","12"))
        assertEquals(-1, comparePair("7","8"))
        assertEquals(1, comparePair("4","3"))
    }

    @Test
    fun listList() {
        assertEquals(0, comparePair("[1,2,3]","[1,2,3]"))
        assertEquals(-1, comparePair("[]","[1]"))
        assertEquals(-1, comparePair("[4,8]","[4,8,12]"))
        assertEquals(-1, comparePair("[4,7]","[4,8,12]"))
        assertEquals(1, comparePair("[34]","[]"))
        assertEquals(1, comparePair("[4,7,13]","[4,7]"))
        assertEquals(1, comparePair("[4,7,13]","[4,7,12]"))
    }
}