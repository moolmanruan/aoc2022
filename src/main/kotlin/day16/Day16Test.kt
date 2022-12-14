package day16

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParsingTest() {
    private val lineA = "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB"
    private val lineB = "Valve BB has flow rate=13; tunnels lead to valve AA"
    private val lineFoo = "Valve FOO has flow rate=234; tunnels lead to valves BAR, BAZ"

    @Test
    fun parseName() {
        assertEquals("AA", valveName(lineA))
        assertEquals("BB", valveName(lineB))
        assertEquals("FOO", valveName(lineFoo))
    }

    @Test
    fun parseRate() {
        assertEquals(0, valveRate(lineA))
        assertEquals(13, valveRate(lineB))
        assertEquals(234, valveRate(lineFoo))
    }

    @Test
    fun parseTunnels() {
        val nameToPos = mapOf(
            "AA" to 0,
            "BB" to 1,
            "DD" to 3,
            "II" to 8,
            "BAR" to 123,
            "BAZ" to 234
        )
        assertEquals(emptyList<Int>(), valveTunnels(lineA, mapOf()))
        assertEquals(listOf(3, 8, 1), valveTunnels(lineA, nameToPos))
        assertEquals(listOf(0), valveTunnels(lineB, nameToPos))
        assertEquals(listOf(123, 234), valveTunnels(lineFoo, nameToPos))
    }

    @Test
    fun parseValves() {
        assertEquals(
            listOf(
                Valve("AA", 12, listOf(0, 1, 1, 2), 0),
                Valve("BB", 0, listOf(3, 0, 2, 1), 1),
                Valve("CC", 43, listOf(1, 2, 0, 1), 2),
                Valve("DD", 3, listOf(2, 1, 1, 0), 3)
            ),
            toValves(
                """
                Valve AA has flow rate=12; tunnels lead to valves BB, CC
                Valve BB has flow rate=0; tunnels lead to valve DD
                Valve CC has flow rate=43; tunnels lead to valves AA, DD
                Valve DD has flow rate=3; tunnels lead to valves BB, CC
                """.trimIndent()
            )
        )
    }
}

class MoveCostTest() {
    @Test
    fun simple() {
        val tunnels = listOf(
            listOf(1),
            listOf(2),
            listOf(0)
        )
        assertEquals(0, moveCostTo(0, 0, tunnels))
        assertEquals(1, moveCostTo(0, 1, tunnels))
        assertEquals(2, moveCostTo(0, 2, tunnels))
    }

    @Test
    fun trickier() {
        val tunnels = listOf(
            listOf(2, 4),
            listOf(0),
            listOf(1, 3),
            listOf(4),
            listOf(3, 0)
        )
        assertEquals(0, moveCostTo(0, 0, tunnels))
        assertEquals(2, moveCostTo(0, 1, tunnels))
        assertEquals(1, moveCostTo(0, 2, tunnels))
        assertEquals(2, moveCostTo(0, 3, tunnels))
        assertEquals(1, moveCostTo(0, 4, tunnels))

        assertEquals(0, moveCostTo(3, 3, tunnels))
        assertEquals(4, moveCostTo(3, 1, tunnels))
    }

    @Test
    fun loops() {
        val tunnels = listOf(
            listOf(1),
            listOf(0),
            listOf(3),
            listOf(4),
            listOf(2)
        )
        assertEquals(null, moveCostTo(0, 3, tunnels))
        assertEquals(null, moveCostTo(4, 1, tunnels))
    }
}

class Day16Test() {
    @Test
    fun next() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vCC = valves[2]
        val vDD = valves[3]

        val maxTicks = 10
        val start = state(
            listOf(entity(valves[0], 0)),
            valves,
            emptyList()
        )
        val next = nextStates(start, maxTicks)

        val want = listOf(
            state(
                listOf(entity(vAA, 1)), // just open
                listOf(vCC, vDD),
                listOf(Pair(1, vAA))
            ),
            state(
                listOf(entity(vCC, 2)), // tunnel + open
                listOf(vAA, vDD),
                listOf(Pair(2, vCC))
            ),
            state(
                listOf(entity(vDD, 3)), // 2 tunnel moves + open
                listOf(vAA, vCC),
                listOf(Pair(3, vDD))
            )
        )
        assertEquals(want, next)
    }

    @Test
    fun next2() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vDD = valves[3]

        val maxTicks = 10
        val start = state(
            listOf(entity(vAA, 1)),
            listOf(vDD),
            listOf(Pair(1, vAA))
        )
        val next = nextStates(start, maxTicks)

        assertEquals(
            listOf(
                state(
                    listOf(entity(vDD, 4)),
                    emptyList(),
                    listOf(Pair(1, vAA), Pair(4, vDD))
                )
            ),
            next
        )
    }

    @Test
    fun nextOutOfTime() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vCC = valves[2]

        val maxTicks = 10
        val start = state(
            listOf(entity(vCC, 6)), // to get to A takes 3 moves + 1 to open it
            listOf(vAA),
            listOf(Pair(1, vCC))
        )
        assertEquals(emptyList<state>(), nextStates(start, maxTicks))
    }

    @Test
    fun nextTwoEntitiesSameTime() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vCC = valves[2]
        val vDD = valves[3]

        val maxTicks = 10
        val elephant = entity(valves[0], 0)
        val start = state(
            listOf(
                entity(valves[0], 0),
                elephant
            ),
            valves,
            emptyList()
        )
        val next = nextStates(start, maxTicks)

        val want = listOf(
            state(
                listOf(
                    entity(vAA, 1),
                    elephant
                ),
                listOf(vCC, vDD),
                listOf(Pair(1, vAA))
            ),
            state(
                listOf(
                    entity(vCC, 2),
                    elephant
                ),
                listOf(vAA, vDD),
                listOf(Pair(2, vCC))
            ),
            state(
                listOf(
                    entity(vDD, 3),
                    elephant
                ),
                listOf(vAA, vCC),
                listOf(Pair(3, vDD))
            )
        )
        assertEquals(want, next)
    }

    @Test
    fun nextWithElephantFirst() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vCC = valves[2]
        val vDD = valves[3]

        val maxTicks = 10
        val me = entity(valves[0], 1)
        val start = state(
            listOf(
                me,
                entity(valves[0], 0)
            ),
            valves,
            emptyList()
        )
        val next = nextStates(start, maxTicks)

        val want = listOf(
            state(
                listOf(
                    me,
                    entity(vAA, 1)
                ),
                listOf(vCC, vDD),
                listOf(Pair(1, vAA))
            ),
            state(
                listOf(
                    me,
                    entity(vCC, 2)
                ),
                listOf(vAA, vDD),
                listOf(Pair(2, vCC))
            ),
            state(
                listOf(
                    me,
                    entity(vDD, 3)
                ),
                listOf(vAA, vCC),
                listOf(Pair(3, vDD))
            )
        )
        assertEquals(want, next)
    }

    @Test
    fun nextTogether() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vCC = valves[2]
        val vDD = valves[3]

        val maxTicks = 10
        val start = state(
            listOf(
                entity(vAA, 1),
                entity(vAA, 0)
            ),
            listOf(vCC, vDD),
            listOf(Pair(1, vAA))
        )
        val next = nextStates(start, maxTicks)

        val want = listOf(
            state(
                listOf(
                    entity(vAA, 1),
                    entity(vCC, 2)
                ),
                listOf(vDD),
                listOf(Pair(1, vAA), Pair(2, vCC))
            ),
            state(
                listOf(
                    entity(vAA, 1),
                    entity(vDD, 3)
                ),
                listOf(vCC),
                listOf(Pair(1, vAA), Pair(3, vDD))
            )
        )

        assertEquals(want, next)
    }

    @Test
    fun nextOutOfTimeMultipleEntities() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vCC = valves[2]
        val vDD = valves[3]

        val maxTicks = 10
        val start = state(
            listOf(
                entity(vCC, 6),
                entity(vAA, 9)
            ), // to get to A takes 3 moves + 1 to open it
            listOf(vAA),
            listOf(Pair(1, vCC), Pair(1, vDD))
        )
        val want = listOf(
            state(
                listOf(entity(vAA, 9)),
                listOf(vAA),
                listOf(Pair(1, vCC), Pair(1, vDD))
            )
        )
        val next = nextStates(start, maxTicks)
        assertEquals(want, next)
        assertEquals(emptyList<state>(), nextStates(next[0], maxTicks))
    }

    @Test
    fun history() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vCC = valves[2]
        val vDD = valves[3]

        val maxTicks = 10
        val s = state(
            listOf(entity(vCC, maxTicks)),
            emptyList(),
            listOf(Pair(3, vDD), Pair(5, vAA), Pair(7, vCC))
        )

        assertEquals(226, s.flow(maxTicks))
        assertEquals(
            """
            1: 0 (+0)
            2: 0 (+0)
            3: 0 (+20)
            4: 20 (+20)
            5: 40 (+33)
            6: 73 (+33)
            7: 106 (+40)
            8: 146 (+40)
            9: 186 (+40)
            10: 226 (+40)
            """.trimIndent(),
            s.replay(maxTicks)
        )
    }

    @Test
    fun historySimultaneous() {
        val input = """
            Valve AA has flow rate=13; tunnels lead to valves BB, CC
            Valve BB has flow rate=0; tunnels lead to valve DD
            Valve CC has flow rate=7; tunnels lead to valve BB
            Valve DD has flow rate=20; tunnels lead to valve AA
        """.trimIndent()
        val valves = toValves(input)
        val vAA = valves[0]
        val vCC = valves[2]
        val vDD = valves[3]

        val maxTicks = 10
        val s = state(
            listOf(entity(vCC, maxTicks)),
            emptyList(),
            listOf(Pair(2, vDD), Pair(5, vAA), Pair(5, vCC))
        )

        assertEquals(260, s.flow(maxTicks))
        assertEquals(
            """
            1: 0 (+0)
            2: 0 (+20)
            3: 20 (+20)
            4: 40 (+20)
            5: 60 (+40)
            6: 100 (+40)
            7: 140 (+40)
            8: 180 (+40)
            9: 220 (+40)
            10: 260 (+40)
            """.trimIndent(),
            s.replay(maxTicks)
        )
    }
}
