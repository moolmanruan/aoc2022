import grid.add

typealias Rock = grid.Coord
typealias Sand = grid.Coord

fun stringToRocks(input: String): Set<Rock> {
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
    return rocks
}

class SandSimulation(val rocks: Set<Rock>, val sandOrigin: grid.Coord, val hasFloor: Boolean = true) {
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
    val rocksSets = input.split("\n").map(::stringToRocks)
    val rocks = mutableSetOf<Rock>()
    for (r in rocksSets) {
        rocks += r
    }

    val simInf = SandSimulation(rocks, grid.Coord(500, 0), false)
    while (simInf.step()) {}
    println("Part 1:$ANSI_BLUE ${simInf.sand.size}$ANSI_WHITE want 901 example 24$ANSI_RESET")

    val simFloor = SandSimulation(rocks, grid.Coord(500, 0))
    while (simFloor.step()) {}
    return simFloor.sand.size.toString() + "$ANSI_WHITE want 24589 example 93"
}
