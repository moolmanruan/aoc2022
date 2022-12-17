package day17

import grid.plus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PatternTest {
    @Test
    fun noPattern() {
        assertEquals(null, findPattern(listOf(1, 2, 3, 5, 4)))
    }

    @Test
    fun simple() {
        assertEquals(Pattern(listOf(1, 2, 3), emptyList()), findPattern(listOf(1, 2, 3, 1, 2, 3)))
        assertEquals(Pattern(listOf(1, 2, 3), listOf(4, 5)), findPattern(listOf(4, 5, 1, 2, 3, 1, 2, 3)))
        assertEquals(Pattern(listOf(1, 2), listOf(4)), findPattern(listOf(4, 1, 2, 1, 2, 1, 2, 1, 2)))
    }
}

class RockMoveTest {
    @Test
    fun left() {
        val rock = ROCKS[0]
        val newRock = rock.move(Move.LEFT)
        assertEquals(newRock.size, rock.size)
        for (piece in rock) {
            assertTrue(piece.plus(grid.Left) in newRock)
        }
    }

    @Test
    fun right() {
        val rock = ROCKS[1]
        val newRock = rock.move(Move.RIGHT)
        assertEquals(newRock.size, rock.size)
        for (piece in rock) {
            assertTrue(piece.plus(grid.Right) in newRock)
        }
    }

    @Test
    fun down() {
        val rock = ROCKS[2]
        val newRock = rock.move(Move.DOWN)
        assertEquals(newRock.size, rock.size)
        for (piece in rock) {
            assertTrue(piece.plus(grid.Down) in newRock)
        }
    }
}
