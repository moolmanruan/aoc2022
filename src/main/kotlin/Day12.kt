import grid.Coord
import grid.add
import grid.distanceL1
import grid.sub

data class MapPos(val letter: Char)

fun MapPos.height(): Int {
    return letter.code - 'a'.code
}

data class Path(val positions: MutableList<Coord>)

fun Path.add(c: Coord): Path {
    val newPositions = positions.toList() + listOf(c)
    return Path(newPositions.toMutableList())
}

fun Path.pos(): Coord {
    return positions.last()
}

fun Path.length(): Int {
    return positions.size
}

fun Path.toString(width: Int, height: Int): String {
    val pathGrid = grid.NewGrid(width, height, " ").toMutableGrid()
    positions.forEachIndexed() { i: Int, c: Coord ->
        if (i < positions.size - 1) {
            when (positions[i + 1].sub(c)) {
                grid.Up -> pathGrid.set(c, "v")
                grid.Down -> pathGrid.set(c, "^")
                grid.Right -> pathGrid.set(c, ">")
                grid.Left -> pathGrid.set(c, "<")
            }
        } else {
            pathGrid.set(c, "X")
        }
    }
    return pathGrid.toString()
}

fun astar(start: Coord, end: Coord, heightMap: grid.Grid<MapPos>): Path? {
    var paths = mutableListOf(
        Path(mutableListOf(start))
    )
    val shortestPath = grid.NewGrid<Path?>(heightMap.width(), heightMap.width(), null).toMutableGrid()

    var thePath: Path? = null

    while (paths.size > 0) {
        val curPath = paths.first()
        paths = paths.drop(1).toMutableList()

        val curPos = curPath.pos()
        val dirs = listOf(
            curPos.add(grid.Up),
            curPos.add(grid.Right),
            curPos.add(grid.Down),
            curPos.add(grid.Left)
        )

        val reachableHeight = heightMap.get(curPos).height() + 1
        dirs.forEach { dir ->
            if (heightMap.contains(dir) &&
                (shortestPath.get(dir) == null || shortestPath.get(dir)!!.length() > curPath.length() + 1) &&
                (heightMap.get(dir).height() <= reachableHeight)
            ) {
                val newPath = curPath.add(dir)
                if (newPath.pos() == end) {
                    thePath = newPath
                }

                if (shortestPath.get(newPath.pos()) == null ||
                    shortestPath.get(newPath.pos())!!.length() > newPath.length()
                ) {
                    shortestPath.set(newPath.pos(), newPath)
                }
                paths.add(newPath)
            }
        }

        if (thePath != null) {
            break
        }

        // sort the paths
        paths.sortBy { it.positions.size + it.pos().distanceL1(end) }
    }
    return thePath
}

fun day12(input: String): String {
    var startPart1 = Coord(0, 0)
    var end = Coord(0, 0)
    val heightMap = grid.NewGridFromStringIndexed(input, "") { cell, pos ->
        when (cell) {
            "S" -> {
                startPart1 = pos
                MapPos('a')
            }
            "E" -> {
                end = pos
                MapPos('z')
            }
            else -> MapPos(cell[0])
        }
    }

    val starts = mutableListOf<Coord>()
    heightMap.forEachCell { c, value ->
        if (value.height() == 0) {
            starts.add(c)
        }
    }

    val bestPaths = mutableListOf<Path>()
    starts.map { {} }
    for (start in starts) {
        val path = astar(start, end, heightMap) ?: continue
        bestPaths.add(path)
    }
    val bestPath = bestPaths.sortedBy { it.length() }.first()
    println(heightMap.toStringCustom { it.letter.toString() })

    // Part 1
    val partOnePath = astar(startPart1, end, heightMap)
    println(partOnePath!!.toString(heightMap.width(), heightMap.height()))
    println("${partOnePath.length() - 1} want 412 (example want 31)")
    // Part 2
    println(bestPath.toString(heightMap.width(), heightMap.height()))
    return "${bestPath.positions.size - 1} want 402 (example want 29)"
}
