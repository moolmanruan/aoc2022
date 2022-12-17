package day17

import grid.Coord
import grid.plus

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
//####
    setOf(
        Coord(0,0),
        Coord(1,0),
        Coord(2,0),
        Coord(3,0)
    ),
//.#.
//###
//.#.
    setOf(
        Coord(1,0),
        Coord(0,1),
        Coord(1,1),
        Coord(2,1),
        Coord(1,2)
    ),
//..#
//..#
//###
    setOf(
        Coord(0,0),
        Coord(1,0),
        Coord(2,0),
        Coord(2,1),
        Coord(2,2)
    ),
//#
//#
//#
//#
    setOf(
        Coord(0, 0),
        Coord(0, 1),
        Coord(0, 2),
        Coord(0, 3)
    ),
//##
//##
    setOf(
        Coord(0, 0),
        Coord(1, 0),
        Coord(0, 1),
        Coord(1, 1)
    ),
)

const val CHAMBER_WIDTH = 7
const val BEGIN_X = 2
const val BEGIN_Y = 3

const val EXAMPLE_ROCKS = 2022
const val EXAMPLE_HEIGHT = 3068

enum class Move {LEFT, RIGHT, DOWN}

data class simulation(val width: Int, val rocks: List<Rock>, val pushes: List<Move>) {
    val stopped = mutableSetOf<Coord>()
    var active: Rock? = null
    var activeIndex = -1
    var pushIndex = -1

    var numRocks = 0
    var ticks = 0

    fun tick() {
        ticks++
        println("Tick #$ticks")

        maybeNewRock()
        if (!push()) {println("BLOCKED")}
        if (!fall()) {
            stopActive()
        }
    }

    fun nextPush():Move {
        pushIndex = (pushIndex+1)%pushes.size
        return pushes[pushIndex]
    }
    fun push():Boolean {
        val p = nextPush()
        println("PUSH $p")
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
        println("FALL")
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
        activeIndex = (activeIndex+1)%rocks.size
        return rocks[activeIndex]
    }
    fun maybeNewRock() {
        if (active == null) {
            active = nextRock().moveBy(Coord(BEGIN_X,height()+ BEGIN_Y))
            println("NEW ROCK $activeIndex $active")
        }
    }

    fun stopActive() {
        println("STOPPED AT $active")
        active?.forEach {stopped.add(it)}
        active = null
        numRocks++
    }

    fun height():Int {
        return if (stopped.isEmpty()) 0 else stopped.maxOf { it.y }+1
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
    val sim = simulation(CHAMBER_WIDTH, ROCKS, input.toPushes())

    while (sim.numRocks < EXAMPLE_ROCKS) {
//    while (sim.numRocks < 3) {
//    for (s in 1 ..5){
        sim.tick()
    }
    return "${sim.height()} want $EXAMPLE_HEIGHT"
}