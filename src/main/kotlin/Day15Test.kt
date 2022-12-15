import grid.Coord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Day15Test() {
    @Test
    fun twoAway() {
        val res = sensorCoverage(
            Coord(0, 0),
            listOf(Coord(1, 1), Coord(-1, -2))
        )
        assertEquals(13, res.size)
        assertTrue(Coord(-2, 0) in res)
        assertTrue(Coord(-2, -1) !in res)
    }

//    @Test
//    fun fiveAway() {
//        val sensor = Coord(1, -2)
//        val res = sensorCoverage(
//            sensor,
//            listOf(
//                Coord(1, 1),
//                Coord(-1, -2),
//            )
//        )
//        assertEquals(13, res.size)
//        assertTrue(Coord(-2, 0) in res)
//        assertTrue(Coord(-2, -1) !in res)
//    }
}
