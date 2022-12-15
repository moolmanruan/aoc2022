import grid.Coord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day15Test() {
    @Test
    fun one() {
        val si = SensorInfo(Coord(3, 4), Coord(10, 20))
        assertEquals(Coord(6, 0), si.mirrorXRight(Coord(0, 0)))
        assertEquals(Coord(8, 10), si.mirrorXRight(Coord(-2, 10)))
        assertEquals(Coord(6, -5), si.mirrorXRight(Coord(6, -5)))
        assertEquals(Coord(8, 10), si.mirrorXRight(Coord(8, 10)))
    }

    @Test
    fun borders1() {
        val si = SensorInfo(Coord(0, 0), Coord(1, 1))
        assertEquals(
            listOf(
                Coord(-2, 0),
                Coord(2, 0),
                Coord(-1, -1),
                Coord(-1, 1),
                Coord(1, -1),
                Coord(1, 1),
                Coord(0, -2),
                Coord(0, 2)
            ),
            si.border()
        )
    }

    @Test
    fun borders2() {
        val si = SensorInfo(Coord(1, -1), Coord(1, 1))
        assertEquals(
            listOf(
                Coord(-1, -1),
                Coord(3, -1),
                Coord(0, -2),
                Coord(0, 0),
                Coord(2, -2),
                Coord(2, 0),
                Coord(1, -3),
                Coord(1, 1)
            ),
            si.border()
        )
    }
}
