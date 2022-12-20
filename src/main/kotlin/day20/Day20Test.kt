package day20

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day20Test {
    @Test
    fun decode() {
        assertEquals(3, coords(listOf(1, 2, -3, 4, 0, 3, -2)))
    }

    @Test
    fun moveNumbers() {
        var l = mutableListOf<Pair<Int, Long>>()
        l = listOf(
            Pair(1, 1.toLong()),
            Pair(2, 2.toLong()),
            Pair(3, (-3).toLong()),
            Pair(4, 3.toLong())
        ).toMutableList()
        moveNumber(l, 1)
        assertEquals(
            listOf(
                Pair(2, 2),
                Pair(1, 1),
                Pair(3, -3),
                Pair(4, 3)
            ),
            l
        )

        l = listOf(
            Pair(2, 2.toLong()),
            Pair(1, 1.toLong()),
            Pair(3, (-3).toLong()),
            Pair(4, 3.toLong())
        ).toMutableList()
        moveNumber(l, 2)
        assertEquals(
            listOf(
                Pair(1, 1),
                Pair(3, -3),
                Pair(2, 2),
                Pair(4, 3)
            ),
            l
        )

        l = listOf(
            Pair(1, 1.toLong()),
            Pair(3, (-3).toLong()),
            Pair(2, 2.toLong()),
            Pair(4, 3.toLong())
        ).toMutableList()
        moveNumber(l, 3)
        assertEquals(
            listOf(
                Pair(1, 1),
                Pair(3, -3),
                Pair(2, 2),
                Pair(4, 3)
            ),
            l
        )

        moveNumber(l, 4)
        assertEquals(
            listOf(
                Pair(1, 1),
                Pair(3, -3),
                Pair(2, 2),
                Pair(4, 3)
            ),
            l
        )

        val lla = listOf(
            Pair(1, 1.toLong()),
            Pair(2, (-7).toLong()),
            Pair(3, (-3).toLong()),
            Pair(4, 3.toLong())

        ).toMutableList()
        moveNumber(lla, 2)
        assertEquals(
            listOf(
                Pair(1, 1),
                Pair(3, -3),
                Pair(4, 3),
                Pair(2, -7)
            ),
            lla
        )

        val llb = listOf(
            Pair(1, 1.toLong()),
            Pair(2, 7.toLong()),
            Pair(3, (-3).toLong()),
            Pair(4, 3.toLong())
        ).toMutableList()
        moveNumber(llb, 2)
        assertEquals(
            listOf(
                Pair(1, 1),
                Pair(3, -3),
                Pair(2, 7),
                Pair(4, 3)
            ),
            llb
        )
    }
}
