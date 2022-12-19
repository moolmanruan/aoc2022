package day19

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day19Test {
    @Test
    fun increaseResourcesSimple() {
        val state = State(
            1,
            Robots(1, 0, 0, 0),
            Resources(0, 0, 0, 0)
        )
        val next = nextStates(
            state,
            RobotCosts(
                Resources(10, 10, 10, 10),
                Resources(10, 10, 10, 10),
                Resources(10, 10, 10, 10),
                Resources(10, 10, 10, 10)
            )
        )
        assertEquals(
            listOf(
                State(
                    2,
                    Robots(1, 0, 0, 0),
                    Resources(1, 0, 0, 0)
                )
            ),
            next
        )
    }

    @Test
    fun increaseResourcesMore() {
        val state = State(
            5,
            Robots(4, 5, 7, 3),
            Resources(1, 2, 3, 4)
        )
        val next = nextStates(
            state,
            RobotCosts(
                Resources(10, 10, 10, 10),
                Resources(10, 10, 10, 10),
                Resources(10, 10, 10, 10),
                Resources(10, 10, 10, 10)
            )
        )
        assertEquals(
            listOf(
                State(
                    6, // 5 + 1
                    Robots(4, 5, 7, 3),
                    Resources(5, 7, 10, 7)
                )
            ),
            next
        )
    }

    @Test
    fun scheduleBuilds() {
        val state = State(
            2,
            Robots(1, 0, 0, 0),
            Resources(2, 0, 0, 0)
        )
        val next = nextStates(
            state,
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            )
        )
        assertEquals(
            listOf(
                State(
                    3,
                    Robots(1, 1, 0, 0),
                    Resources(1, 0, 0, 0)
                ),
                State(
                    3,
                    Robots(3, 0, 0, 0),
                    Resources(1, 0, 0, 0)
                )
            ),
            next
        )
    }
}

class PossibleBuildTest {
    private val defaultCosts = RobotCosts(
        Resources(4, 0, 0, 0),
        Resources(2, 0, 0, 0),
        Resources(3, 14, 0, 0),
        Resources(3, 0, 7, 0)
    )

    @Test
    fun nothing() {
        val got = possibleBuild(
            defaultCosts,
            Resources(0, 0, 0, 0)
        )
        assertEquals(
            listOf(
                Robots(0, 0, 0, 0)
            ),
            got
        )
    }

    @Test
    fun oneNonZero() {
        val got = possibleBuild(
            defaultCosts,
            Resources(2, 0, 0, 0)
        )
        assertEquals(
            listOf(
                Robots(0, 1, 0, 0),
                Robots(0, 0, 0, 0)
            ),
            got
        )
    }

    @Test
    fun twoOptions() {
        val got = possibleBuild(
            defaultCosts,
            Resources(4, 0, 0, 0)
        )
        assertEquals(
            listOf(
                Robots(0, 1, 0, 0),
                Robots(1, 0, 0, 0),
                Robots(0, 0, 0, 0)
            ),
            got
        )
    }

    @Test
    fun manyOptions() {
        val got = possibleBuild(
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(1, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            ),
            Resources(2, 1, 1, 0)
        )
        assertEquals(
            listOf(
                Robots(0, 0, 0, 1),
                Robots(0, 0, 1, 0),
                Robots(0, 1, 0, 0),
                Robots(1, 0, 0, 0),
                Robots(0, 0, 0, 0)
            ),
            got
        )
    }

    @Test
    fun realOptions() {
        val got = possibleBuild(
            RobotCosts(
                Resources(4, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(3, 7, 0, 0),
                Resources(3, 0, 14, 0)
            ),
            Resources(20, 20, 20, 0)
        )
        assertEquals(
            listOf(
                Robots(0, 0, 0, 1),
                Robots(0, 0, 1, 0),
                Robots(0, 1, 0, 0),
                Robots(1, 0, 0, 0),
                Robots(0, 0, 0, 0)
            ),
            got
        )
    }
}

class CanBuildTest {
    @Test
    fun nothing() {
        val got = canBuild(
            Robots(10, 10, 10, 10),
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            ),
            Resources(0, 0, 0, 0)
        )
        assertEquals(false, got)
    }

    @Test
    fun twoOreRobotsCosting1OreWith2Ore() {
        var got = canBuild(
            Robots(2, 0, 0, 0),
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            ),
            Resources(2, 0, 0, 0)
        )
        assertEquals(true, got)
        got = canBuild(
            Robots(3, 0, 0, 0),
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            ),
            Resources(2, 0, 0, 0)
        )
        assertEquals(false, got)
    }

    @Test
    fun oneClayRobotCosting2OreWith2Ore() {
        var got = canBuild(
            Robots(0, 1, 0, 0),
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            ),
            Resources(2, 0, 0, 0)
        )
        assertEquals(true, got)
        got = canBuild(
            Robots(0, 2, 0, 0),
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            ),
            Resources(2, 0, 0, 0)
        )
        assertEquals(false, got)
    }

    @Test
    fun many() {
        var got = canBuild(
            Robots(10, 2, 4, 3),
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            ),
            Resources(21, 4, 3, 0)
        )
        assertEquals(true, got)
        got = canBuild(
            Robots(10, 2, 4, 3),
            RobotCosts(
                Resources(1, 0, 0, 0),
                Resources(2, 0, 0, 0),
                Resources(1, 1, 0, 0),
                Resources(1, 0, 1, 0)
            ),
            Resources(21, 4, 2, 0)
        )
        assertEquals(false, got)
    }
}
