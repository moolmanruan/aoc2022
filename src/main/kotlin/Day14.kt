import grid.Coord
import grid.add
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
            curPos = curPos.add(step)
        }
    }
    rocks.add(corners.last())
    return rocks
}

class SandSimulation(val rocks: Set<Rock>, val sandOrigin: Coord, val hasFloor: Boolean = true) {
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
                val p = sandPos.add(dir)
                return p !in rocks && p !in sand
            }

            sandPos = when {
                isOpen(grid.Up) -> sandPos.add(grid.Up)
                isOpen(grid.UpLeft) -> sandPos.add(grid.UpLeft)
                isOpen(grid.UpRight) -> sandPos.add(grid.UpRight)
                else -> break
            }
        }
        sand.add(sandPos)
        return true
    }
}

fun day14(input: String): String {
    val rocksSets = input.split("\n").map(::stringToRocks)
    val rocks = mutableSetOf<Rock>()
    for (r in rocksSets) {
        rocks += r
    }

    val simInf = SandSimulation(rocks, Coord(500, 0), false)
    while (simInf.step()) {}
    println("Part 1:$ANSI_BLUE ${simInf.sand.size}$ANSI_WHITE want 901 example 24$ANSI_RESET")

    val simFloor = SandSimulation(rocks, Coord(500, 0))
    while (simFloor.step()) {}
    return simFloor.sand.size.toString() + "$ANSI_WHITE want 24589 example 93"
}
