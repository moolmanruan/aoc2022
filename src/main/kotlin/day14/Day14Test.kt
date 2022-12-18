package day14

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestStringsToRock() {
    @Test
    fun lineOne() {
        assertEquals(
            setOf(
                Rock(498, 4),
                Rock(498, 5),
                Rock(498, 6),
                Rock(497, 6),
                Rock(496, 6)
            ),
            stringToRocks("498,4 -> 498,6 -> 496,6")
        )
    }

    @Test
    fun lineTwo() {
        assertEquals(
            setOf(
                Rock(503, 4),
                Rock(502, 4),
                Rock(502, 5),
                Rock(502, 6),
                Rock(502, 7),
                Rock(502, 8),
                Rock(502, 9),
                Rock(501, 9),
                Rock(500, 9),
                Rock(499, 9),
                Rock(498, 9),
                Rock(497, 9),
                Rock(496, 9),
                Rock(495, 9),
                Rock(494, 9)
            ),
            stringToRocks("503,4 -> 502,4 -> 502,9 -> 494,9")
        )
    }
}

class TestSandSimulation() {
    @Test
    fun simple() {
        val sim = SandSimulation(
            setOf(
                Rock(-2, 5),
                Rock(-1, 5),
                Rock(0, 5),
                Rock(1, 5),
                Rock(2, 5)
            ),
            grid.Coord(0, 0)
        )
        assertEquals(true, sim.step())
        val expectedSand = mutableSetOf(Sand(0, 4))
        assertEquals(expectedSand, sim.sand)

        assertEquals(true, sim.step())
        expectedSand.add(Sand(-1, 4))
        assertEquals(expectedSand, sim.sand)

        assertEquals(true, sim.step())
        expectedSand.add(Sand(1, 4))
        assertEquals(expectedSand, sim.sand)

        assertEquals(true, sim.step())
        expectedSand.add(Sand(0, 3))
        assertEquals(expectedSand, sim.sand)
    }

    @Test
    fun infinite() {
        val sim = SandSimulation(
            setOf(Rock(1, -1), Rock(6, 2), Rock(-13, -5)),
            grid.Coord(1, -5),
            false
        )
        assertEquals(false, sim.step())
    }

    @Test
    fun floor() {
        val sim = SandSimulation(
            setOf(Rock(1, -1), Rock(6, 2), Rock(-13, -5)),
            grid.Coord(1, -5)
        )
        assertEquals(true, sim.step())
        assertEquals(mutableSetOf(Sand(0, 3)), sim.sand)
    }

    @Test
    fun blocked() {
        val sim = SandSimulation(
            setOf(Rock(100, 0)),
            grid.Coord(1, 0)
        )
        assertEquals(true, sim.step())
        val expectedSand = mutableSetOf(Sand(1, 1))
        assertEquals(expectedSand, sim.sand)

        assertEquals(true, sim.step())
        expectedSand.add(Sand(0, 1))
        assertEquals(expectedSand, sim.sand)

        assertEquals(true, sim.step())
        expectedSand.add(Sand(2, 1))
        assertEquals(expectedSand, sim.sand)

        assertEquals(true, sim.step())
        expectedSand.add(Sand(1, 0))
        assertEquals(expectedSand, sim.sand)

        assertEquals(false, sim.step())
    }
}
