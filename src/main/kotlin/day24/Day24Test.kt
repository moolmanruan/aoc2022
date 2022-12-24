package day24

import grid.Coord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day24Test {
    @Test
    fun fromString() {
        assertEquals(
            BlizzardState(
                Coord(5, 5),
                listOf(
                    Blizzard(Direction.LEFT, Coord(4, 1)),
                    Blizzard(Direction.RIGHT, Coord(2, 2)),
                    Blizzard(Direction.UP, Coord(1, 4)),
                    Blizzard(Direction.DOWN, Coord(4, 5))
                )
            ),
            """
            #.#####
            #...<.#
            #.>...#
            #.....#
            #^....#
            #...v.#
            #####.#
            """.trimIndent().toState()
        )
    }

    @Test
    fun asString() {
        assertEquals(
            """
            #######
            #...<.#
            #.>...#
            #...E.#
            #^....#
            #...v.#
            #######
            """.trimIndent(),
            BlizzardState(
                Coord(5, 5),
                listOf(
                    Blizzard(Direction.LEFT, Coord(4, 1)),
                    Blizzard(Direction.RIGHT, Coord(2, 2)),
                    Blizzard(Direction.UP, Coord(1, 4)),
                    Blizzard(Direction.DOWN, Coord(4, 5))
                )
            ).asString(Coord(4, 3))
        )

        assertEquals(
            """
            #######
            #.....#
            #.2...#
            #...E.#
            #.....#
            #...3.#
            #######
            """.trimIndent(),
            BlizzardState(
                Coord(5, 5),
                listOf(
                    Blizzard(Direction.LEFT, Coord(2, 2)),
                    Blizzard(Direction.RIGHT, Coord(2, 2)),
                    Blizzard(Direction.UP, Coord(4, 5)),
                    Blizzard(Direction.DOWN, Coord(4, 5)),
                    Blizzard(Direction.DOWN, Coord(4, 5))
                )
            ).asString(
                Coord(4, 3)
            )
        )
    }

    @Test
    fun blizzardNext() {
        assertEquals(
            """
            #######
            #..<..#
            #..>..#
            #^....#
            #.....#
            #...v.#
            #######
            """.trimIndent(),
            """
            #######
            #...<.#
            #.>...#
            #.....#
            #^..v.#
            #.....#
            #######
            """.trimIndent().toState().next().asString(null)
        )
        assertEquals(
            """
            ######
            #..v<#
            #>...#
            #.^..#
            ######
            """.trimIndent(),
            """
            ######
            #<^..#
            #...>#
            #..v.#
            ######
            """.trimIndent().toState().next().asString(null)
        )
    }

    @Test
    fun nextMoves() {
        var bs = """
            #####
            #.<.#
            #####
        """.trimIndent().toState()
        assertEquals(
            listOf(
                Coord(1, 1),
                Coord(1, 0)
            ),
            nextStates(Coord(1, 0), bs)
        )

        bs = """
            #####
            #vvv#
            #.v.#
            #####
        """.trimIndent().toState()
        assertEquals(
            emptyList<Coord>(),
            nextStates(Coord(2, 1), bs)
        )

        bs = """
            #####
            #.vv#
            #.v.#
            #####
        """.trimIndent().toState()
        assertEquals(
            listOf(Coord(1, 1)),
            nextStates(Coord(2, 1), bs)
        )

        bs = """
            ####
            #>>#
            #<<#
            ####
        """.trimIndent().toState()
        assertEquals(
            listOf(Coord(2, 3)),
            nextStates(Coord(2, 2), bs)
        )
    }

    @Test
    fun asStringImage() {
        var bs = """
            #.##
            #>v#
            #^<#
            ##.#
        """.trimIndent().toState()
//        assertEquals(
//            """
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,e,x,x,x,r,e,x,x,x,e,w,w,w,w,w
//            w,w,w,w,w,e,x,x,x,r,e,x,x,x,e,w,w,w,w,w
//            w,w,w,w,w,e,x,x,x,r,e,x,x,x,e,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,d,d,d,e,w,w,w,w,w
//            w,w,w,w,w,e,u,u,u,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,e,x,x,x,e,l,x,x,x,e,w,w,w,w,w
//            w,w,w,w,w,e,x,x,x,e,l,x,x,x,e,w,w,w,w,w
//            w,w,w,w,w,e,x,x,x,e,l,x,x,x,e,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            """.trimIndent(),
//            bs.asStringImage()
//        )
//        bs = """
//            ####
//            #.<#
//            #^.#
//            ####
//        """.trimIndent().toState()
//        assertEquals(
//            """
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
//            w,w,w,w,w,e,u,u,u,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,l,x,x,x,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,l,x,x,x,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,l,x,x,x,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
//            """.trimIndent(),
//            bs.next().asStringImage()
//        )

        bs = """
            ####
            #..#
            #..#
            ####
        """.trimIndent().toState()
        assertEquals(
            """
            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,w,w,w,w,w,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,o,o,o,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,o,o,o,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,o,o,o,e,w,w,w,w,w
            w,w,w,w,w,e,e,e,e,e,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
            w,w,w,w,w,w,w,w,w,w,e,e,e,e,e,w,w,w,w,w
            """.trimIndent(),
            bs.asStringImage(Coord(2, 2))
        )
    }
}
