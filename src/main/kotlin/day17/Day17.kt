package day17

import grid.Coord
import grid.plus
import output.printAnswer

typealias Rock = Set<Coord>

fun Rock.move(dir: Move): Rock {
    val c = when (dir) {
        Move.LEFT -> grid.Left
        Move.RIGHT -> grid.Right
        Move.DOWN -> grid.Down
    }
    return moveBy(c)
}

fun Rock.moveBy(c: Coord): Rock {
    return this.map { it.plus(c) }.toSet()
}

val ROCKS = listOf(
// ####
    setOf(
        Coord(0, 0),
        Coord(1, 0),
        Coord(2, 0),
        Coord(3, 0)
    ),
// .#.
// ###
// .#.
    setOf(
        Coord(1, 0),
        Coord(0, 1),
        Coord(1, 1),
        Coord(2, 1),
        Coord(1, 2)
    ),
// ..#
// ..#
// ###
    setOf(
        Coord(0, 0),
        Coord(1, 0),
        Coord(2, 0),
        Coord(2, 1),
        Coord(2, 2)
    ),
// #
// #
// #
// #
    setOf(
        Coord(0, 0),
        Coord(0, 1),
        Coord(0, 2),
        Coord(0, 3)
    ),
// ##
// ##
    setOf(
        Coord(0, 0),
        Coord(1, 0),
        Coord(0, 1),
        Coord(1, 1)
    )
)

const val CHAMBER_WIDTH = 7
const val BEGIN_X = 2
const val BEGIN_Y = 3

const val PART1_ROCKS = 2022
const val PART2_ROCKS = 1000000000000
const val PART1_EXAMPLE_ANSWER = 3068
const val PART1_PROBLEM_ANSWER = 3191
const val PART2_EXAMPLE_ANSWER = 1514285714288
const val PART2_PROBLEM_ANSWER = 1572093023267

enum class Move { LEFT, RIGHT, DOWN }

data class simulation(val width: Int, val rocks: List<Rock>, val pushes: List<Move>) {
    private val stopped = mutableSetOf<Coord>()
    private var active: Rock? = null
    private var activeIndex = -1
    private var pushIndex = -1
    var numRocks = 0

    fun tick() {
        maybeNewRock()
        while (active != null) {
            push()
            if (!fall()) {
                stopActive()
            }
        }
    }

    fun nextPush(): Move {
        pushIndex = (pushIndex + 1) % pushes.size
        return pushes[pushIndex]
    }

    fun push(): Boolean {
        val p = nextPush()
        val pushedActive = active?.move(p) ?: error("No rock to push")

        // Can't push past wall
        for (piece in pushedActive) {
            if (piece.x < 0 || piece.x >= CHAMBER_WIDTH) {
                return false
            }
        }
        // Can't push if intersects
        if (pushedActive.intersect(stopped).isNotEmpty()) {
            return false
        }
        // If we could push it, update the active rock
        active = pushedActive
        return true
    }

    fun fall(): Boolean {
        val fallActive = active?.move(Move.DOWN) ?: error("No rock to fall")
        // Can't fall past the floor
        for (piece in fallActive) {
            if (piece.y < 0) {
                return false
            }
        }
        // Can't fall if intersects
        if (fallActive.intersect(stopped).isNotEmpty()) {
            return false
        }
        active = fallActive
        return true
    }

    fun nextRock(): Rock {
        activeIndex = (activeIndex + 1) % rocks.size
        return rocks[activeIndex]
    }

    fun maybeNewRock() {
        if (active == null) {
            active = nextRock().moveBy(Coord(BEGIN_X, height() + BEGIN_Y))
        }
    }

    fun stopActive() {
        active!!.forEach { stopped.add(it) }
        active = null
        numRocks++
    }

    fun height(): Int {
        return if (stopped.isEmpty()) 0 else stopped.maxOf { it.y } + 1
    }
}

fun String.toPushes(): List<Move> {
    return this.windowed(1).map {
        when (it) {
            ">" -> Move.RIGHT
            "<" -> Move.LEFT
            else -> error("Can't parse $it as a Push")
        }
    }
}

fun run(input: String, stage: String): String {
    part1(input, stage)
    part2(input, stage)
    return ""
}

fun part1(input: String, stage: String) {
    val sim = simulation(CHAMBER_WIDTH, ROCKS, input.toPushes())
    while (sim.numRocks < PART1_ROCKS) { sim.tick() }
    val want = if (stage == "problem") PART1_PROBLEM_ANSWER else PART1_EXAMPLE_ANSWER
    printAnswer(sim.height(), want, "Part 1")
}

fun part2(input: String, stage: String) {
    val sim = simulation(CHAMBER_WIDTH, ROCKS, input.toPushes())
    var pattern: Pattern? = null
    while (sim.numRocks < PART2_ROCKS) {
        sim.tick()
        pattern = findRepeatingPattern(sim)
        if (pattern != null) {
            break
        }
    }

    pattern = pattern!!
    val rocksBeforePattern = pattern.startValues.size.toLong()
    val rocksInPattern = pattern.values.size.toLong()
    val repetitions = (PART2_ROCKS - rocksBeforePattern) / rocksInPattern
    val rocksRemaining = PART2_ROCKS - rocksBeforePattern - repetitions * rocksInPattern

    val height = pattern.startValues.sum().toLong() +
        pattern.values.sum().toLong() * repetitions +
        pattern.values.take(rocksRemaining.toInt()).sum()

    val want = if (stage == "problem") PART2_PROBLEM_ANSWER else PART2_EXAMPLE_ANSWER
    printAnswer(height, want, "Part 2")
}

var singleHeightDiffs = mutableListOf<Int>()
var lastSingleHeight = 0
var heightDiffs = mutableListOf<Int>()
var lastHeight = 0

fun findRepeatingPattern(sim: simulation): Pattern? {
    // Track the diffs for single rocks
    val newSingleHeight = sim.height()
    val singleHeightDiff = newSingleHeight - lastSingleHeight
    lastSingleHeight = newSingleHeight
    singleHeightDiffs.add(singleHeightDiff)

    // And track the diffs for sets of rocks
    if (sim.numRocks % ROCKS.size == 0) {
        val newHeight = sim.height()
        val heightDiff = newHeight - lastHeight
        lastHeight = newHeight
        heightDiffs.add(heightDiff)

        // If we find a repeating pattern in rock sets...
        val p = findPattern(heightDiffs) ?: return null
        // build up a pattern for single rocks and return it
        return Pattern(
            singleHeightDiffs.takeLast(p.values.size * ROCKS.size),
            singleHeightDiffs.take(singleHeightDiffs.size % (p.values.size * ROCKS.size))
        )
    }
    return null
}

data class Pattern(val values: List<Int>, val startValues: List<Int>)
fun findPattern(input: List<Int>): Pattern? {
    for (s in 2..input.size / 2) {
        val reps = (input.size / s)
        val lastPart = input.takeLast(s * reps)
        val p = lastPart.take(s)
        if (lastPart.windowed(s, s).all { it == p }) {
            return Pattern(p, input.take(input.size - s * reps))
        }
    }
    return null
}
