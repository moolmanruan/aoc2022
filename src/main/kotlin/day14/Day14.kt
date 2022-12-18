package day14

import grid.Coord
import grid.plus
import output.printAnswer
import kotlin.math.sign

typealias Rock = Coord
typealias Sand = Coord

fun stringToRocks(input: String): Set<Rock> {
    val corners = input.split(" -> ").map {
        val xy = it.split(",")
        Coord(xy.first().toInt(), xy.last().toInt())
    }
    val rocks = mutableSetOf<Rock>()
    for (ci in 0.until(corners.size - 1)) {
        val start = corners[ci]
        val end = corners[ci + 1]
        val step = Coord((end.x - start.x).sign, (end.y - start.y).sign)
        var curPos = start
        while (curPos != end) {
            rocks.add(curPos)
            curPos += step
        }
    }
    rocks.add(corners.last())
    return rocks
}

class SandSimulation(private val rocks: Set<Rock>, private val sandOrigin: Coord, private val hasFloor: Boolean = true) {
    val sand = mutableSetOf<Sand>()

    // step adds one sand. Returns whether the sand can't be added or if it doesn't come to rest.
    fun step(): Boolean {
        if (sandOrigin in sand) {
            return false
        }

        val maxY = if (hasFloor) rocks.maxBy { it.y }.y + 2 else rocks.maxBy { it.y }.y

        var sandPos = sandOrigin
        while (true) {
            if (hasFloor && sandPos.y >= maxY - 1) {
                break
            }
            if (!hasFloor && sandPos.y > maxY) {
                return false
            }

            val isOpen = fun(dir: Coord): Boolean {
                val p = sandPos + dir
                return p !in rocks && p !in sand
            }

            sandPos = when {
                isOpen(grid.Up) -> sandPos + grid.Up
                isOpen(grid.UpLeft) -> sandPos + grid.UpLeft
                isOpen(grid.UpRight) -> sandPos + grid.UpRight
                else -> break
            }
        }
        sand.add(sandPos)
        return true
    }
}

fun run(input: String, stage: String) {
    val rocksSets = input.split("\n").map(::stringToRocks)
    val rocks = mutableSetOf<Rock>()
    for (r in rocksSets) {
        rocks += r
    }

    val simInf = SandSimulation(rocks, Coord(500, 0), false)
    while (simInf.step()) {}
    val want1 = if (stage == "problem") 901 else 24
    printAnswer(simInf.sand.size, want1, "Part 1")

    val simFloor = SandSimulation(rocks, Coord(500, 0))
    while (simFloor.step()) {}
    val want2 = if (stage == "problem") 24589 else 93
    printAnswer(simFloor.sand.size, want2, "Part 2")
}
