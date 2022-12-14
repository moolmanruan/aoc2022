import grid.Coord
import grid.add

typealias Rock = grid.Coord
typealias Sand = grid.Coord

fun stringToRocks(input: String): List<Rock> {
    val corners = input.split(" -> ").map {
        val xy = it.split(",")
        grid.Coord(xy.first().toInt(), xy.last().toInt())
    }
    val rocks = mutableSetOf<Rock>()
    for (ci in 0.until(corners.size - 1)) {
        val start = corners[ci]
        val end = corners[ci + 1]

        if (start.x == end.x) {
            val up = end.y >= start.y
            val indices: IntRange = if (up) start.y..end.y else end.y..start.y
            for (y in if (up) indices else indices.reversed()) {
                rocks.add(Rock(start.x, y))
            }
        } else {
            val up = end.x >= start.x
            val indices = if (up) start.x..end.x else end.x..start.x
            for (x in if (up) indices else indices.reversed()) {
                rocks.add(Rock(x, start.y))
            }
        }
    }
    rocks.add(corners.last())
    return rocks.toList()
}

class SandSimulation(val rocks: List<Rock>, val sandOrigin: grid.Coord) {
    val sand = mutableListOf<Sand>()

    // step adds on sand. Returns whether the sand came to a stop.
    fun step(): Boolean {
        val maxY = rocks.maxBy { it.y }.y

        var sandPos = sandOrigin
        while (true) {
            if (sandPos.y > maxY) {
                return false
            }
            if (sandPos.add(grid.Up) !in rocks &&
                sandPos.add(grid.Up) !in sand
            ) {
                sandPos = sandPos.add(grid.Up)
            } else if (sandPos.add(grid.UpLeft) !in rocks &&
                sandPos.add(grid.UpLeft) !in sand
            ) {
                sandPos = sandPos.add(grid.UpLeft)
            } else if (sandPos.add(grid.UpRight) !in rocks &&
                sandPos.add(grid.UpRight) !in sand
            ) {
                sandPos = sandPos.add(grid.UpRight)
            } else {
                break
            }
        }
        sand.add(sandPos)
        return true
    }
}

fun day14(input: String): String {
    val rocksList = input.split("\n").map(::stringToRocks)
    val rocks = mutableListOf<Rock>()
    for (r in rocksList) {
        rocks += r
    }
    val sim = SandSimulation(rocks, Coord(500, 0))
    while (sim.step()) {}
    return sim.sand.size.toString()
}
