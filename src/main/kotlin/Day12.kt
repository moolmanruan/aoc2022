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

fun day12(input: String): String {
    var start = Coord(0, 0)
    var end = Coord(0, 0)
    val heightMap = grid.NewGridFromStringIndexed(input, "") { cell, pos ->
        when (cell) {
            "S" -> {
                start = pos
                MapPos('a')
            }
            "E" -> {
                end = pos
                MapPos('z')
            }
            else -> MapPos(cell[0])
        }
    }
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

    if (thePath == null) {
        return "Path not found"
    }
    val path = thePath!!
    val pathGrid = grid.NewGrid(heightMap.width(), heightMap.height(), " ").toMutableGrid()
    path.positions.forEachIndexed() { i: Int, c: Coord ->
        if (i < path.positions.size - 1) {
            when (path.positions[i + 1].sub(c)) {
                grid.Up -> pathGrid.set(c, "v")
                grid.Down -> pathGrid.set(c, "^")
                grid.Right -> pathGrid.set(c, ">")
                grid.Left -> pathGrid.set(c, "<")
            }
        } else {
            pathGrid.set(c, "X")
        }
    }

    println(heightMap.toStringCustom { it.letter.toString() })
    println(pathGrid.toString())

    return "${path.positions.size - 1} want 412 (example want 31)"
}
