package day18

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day18Test() {
    @Test
    fun fillCavity1() {
        data class case(val init: Vec3, val want: Set<Vec3>)

        val cases = listOf(
            case(Vec3(0, 0, 0), setOf(Vec3(0, 0, 0))),
            case(Vec3(2, 0, 0), emptySet())
        )

        for (c in cases) {
            val drops = setOf(
                Vec3(1, 0, 0),
                Vec3(-1, 0, 0),
                Vec3(0, 1, 0),
                Vec3(0, -1, 0),
                Vec3(0, 0, 1),
                Vec3(0, 0, -1)
            )
            val got = cavity(c.init, drops, drops.size)
            assertEquals(c.want, got)
        }
    }

    @Test
    fun fillCavity2() {
        val got = cavity(
            Vec3(0, 0, 0),
            setOf(
                Vec3(2, 0, 0),
                Vec3(1, 1, 0),
                Vec3(1, -1, 0),
                Vec3(1, 0, 1),
                Vec3(1, 0, -1),
                Vec3(-1, 0, 0),
                Vec3(0, 1, 0),
                Vec3(0, -1, 0),
                Vec3(0, 0, 1),
                Vec3(0, 0, -1)
            ),
            6
        )
        assertEquals(
            setOf(
                Vec3(0, 0, 0),
                Vec3(1, 0, 0)
            ),
            got
        )
    }
}
