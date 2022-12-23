package day23

import grid.Coord
import grid.plus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day23Test {
    @Test
    fun singleElf() {
        val e1 = Coord(0, 0)
        val elves = listOf(e1)
        assertEquals(
            listOf(e1),
            nextMoves(elves, 0)
        )
    }

    @Test
    fun moreElves() {
        val e1 = Coord(2, 2)
        val e2 = Coord(3, 2)
        val e3 = Coord(2, 1)
        val e4 = Coord(20, -10)
        val elves = listOf(e1, e2, e3, e4)
        assertEquals(
            listOf(
                e1 + grid.Up,
                e2 + grid.Up,
                e3 + grid.Down,
                e4
            ),
            nextMoves(elves, 0)
        )
        assertEquals(
            listOf(
                e1 + grid.Left,
                e2 + grid.Right,
                e3 + grid.Down,
                e4
            ),
            nextMoves(elves, 1)
        )
        assertEquals(
            listOf(
                e1 + grid.Left,
                e2 + grid.Right,
                e3 + grid.Left,
                e4
            ),
            nextMoves(elves, 2)
        )
        assertEquals(
            listOf(
                e1 + grid.Up,
                e2 + grid.Right,
                e3 + grid.Down,
                e4
            ),
            nextMoves(elves, 3)
        )
        assertEquals(
            listOf(
                e1 + grid.Up,
                e2 + grid.Up,
                e3 + grid.Down,
                e4
            ),
            nextMoves(elves, 4)
        )
    }

    @Test
    fun filterClashes() {
        assertEquals(
            listOf(
                Coord(0, 1),
                Coord(1, 1),
                Coord(0, -1),
                Coord(0, -3),
                Coord(1, -2),
                Coord(10, 10)
            ),
            filterClash(
                listOf(
                    Coord(0, 0),
                    Coord(1, 0),
                    Coord(0, -1),
                    Coord(0, -3),
                    Coord(1, -3),
                    Coord(10, 10)
                ),
                listOf(
                    Coord(0, 1),
                    Coord(1, 1),
                    Coord(0, -2),
                    Coord(0, -2),
                    Coord(1, -2),
                    Coord(10, 10)
                )
            )
        )
    }
}
