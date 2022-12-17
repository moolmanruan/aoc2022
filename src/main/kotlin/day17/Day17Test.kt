package day17

import grid.Coord
import grid.minus
import grid.plus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class Day17Test {
    @Test
    fun simple() {
        assertEquals(1, 1)
    }
}

class RockMoveTest {
    @Test
    fun left() {
        val rock = ROCKS[0]
        val newRock = rock.move(Move.LEFT)
        assertEquals(newRock.size, rock.size)
        for (piece in rock){
            assertTrue(piece.plus(grid.Left) in newRock)
        }
    }

    @Test
    fun right() {
        val rock = ROCKS[1]
        val newRock = rock.move(Move.RIGHT)
        assertEquals(newRock.size, rock.size)
        for (piece in rock){
            assertTrue(piece.plus(grid.Right) in newRock)
        }
    }

    @Test
    fun down() {
        val rock = ROCKS[2]
        val newRock = rock.move(Move.DOWN)
        assertEquals(newRock.size, rock.size)
        for (piece in rock){
            assertTrue(piece.plus(grid.Down) in newRock)
        }
    }
}