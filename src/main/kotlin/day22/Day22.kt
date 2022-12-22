package day22

import grid.Coord
import grid.Grid
import grid.MutableGrid
import grid.plus
import output.printAnswer

enum class PositionType { VOID, EMPTY, BLOCKED, POS }

enum class ActionType { MOVE_FORWARD, TURN_RIGHT, TURN_LEFT }
data class Action(val value: Int, val type: ActionType)

enum class Direction { RIGHT, DOWN, LEFT, UP }

data class State(var position: Coord, var orientation: Direction)

fun String.toActions(): List<Action> {
    var text = this
    val actions = mutableListOf<Action>()
    val dr = """\d+""".toRegex()
    while (text.isNotEmpty()) {
        text = when (text[0]) {
            'R' -> {
                actions.add(Action(1, ActionType.TURN_RIGHT))
                text.removeRange(0, 1)
            }

            'L' -> {
                actions.add(Action(1, ActionType.TURN_LEFT))
                text.removeRange(0, 1)
            }

            else -> {
                val res = dr.find(text)!!.value
                actions.add(Action(res.toInt(), ActionType.MOVE_FORWARD))
                text.removeRange(0, res.length)
            }
        }
    }
    return actions
}

fun run(input: String, stage: String) {
    part1(input, stage)
    part2(input, stage)
}

fun part1(input: String, stage: String) {
    val mapInput = input.split("\n\n").first()
    val moveInput = input.split("\n\n").last().trim()

    val g = grid.NewGridFromString(mapInput, "") {
        when (it) {
            "." -> PositionType.EMPTY
            "#" -> PositionType.BLOCKED
            else -> PositionType.VOID
        }
    }
    val moves = moveInput.toActions()
    val x =
        0.until(g.width()).find { g.get(Coord(it, 0)) == PositionType.EMPTY } ?: throw Exception("Couldn't find start")
    val curState = State(Coord(x, 0), Direction.RIGHT)

    moves.forEach {
        when (it.type) {
            ActionType.MOVE_FORWARD -> {
                val offset = when (curState.orientation) {
                    Direction.RIGHT -> grid.Right
                    Direction.LEFT -> grid.Left
                    Direction.DOWN -> grid.Up
                    Direction.UP -> grid.Down
                }
                for (i in 0.until(it.value)) {
                    var next = curState.position + offset
                    while (g.getWrappedSafe(next, PositionType.VOID) == PositionType.VOID) {
                        next += offset
                    }
                    if (g.getWrappedSafe(next, PositionType.VOID) == PositionType.BLOCKED) {
                        break
                    }
                    curState.position = Coord(next.x.mod(g.width()), next.y.mod(g.height()))
                }
            }

            ActionType.TURN_LEFT -> {
                curState.orientation =
                    Direction.values()[(curState.orientation.ordinal - 1).mod(Direction.values().size)]
            }

            ActionType.TURN_RIGHT -> {
                curState.orientation =
                    Direction.values()[(curState.orientation.ordinal + 1).mod(Direction.values().size)]
            }
        }
    }

    val answer = 1000 * (curState.position.y + 1) + 4 * (curState.position.x + 1) + curState.orientation.ordinal
    val want = if (stage == "problem") 30552 else 6032
    printAnswer(answer, want, "Part 1")
}

data class Cube(
    val sides: List<CubeSide>,
    val neighbours: List<SideNeighbours>
)

fun Cube.get(pos: CubePosition): PositionType {
    return sides[pos.side].grid.get(pos.position)
}

data class CubeSide(
    val offset: Coord,
    val grid: MutableGrid<PositionType>
)

data class Neighbour(
    val index: Int,
    val enterDir: Direction
)

data class SideNeighbours(
    val right: Neighbour,
    val down: Neighbour,
    val left: Neighbour,
    val up: Neighbour
)

data class CubePosition(var side: Int, var position: Coord, var orientation: Direction)

fun part2(input: String, stage: String) {
    val mapInput = input.split("\n\n").first()
    val blockWidth = if (stage == "problem") 50 else 4
    val g = grid.NewGridFromString(mapInput, "") {
        when (it) {
            "." -> PositionType.EMPTY
            "#" -> PositionType.BLOCKED
            else -> PositionType.VOID
        }
    }
    val cubeSides = mutableListOf<CubeSide>()
    for (y in 0 until g.height() step blockWidth) {
        for (x in 0 until g.width() step blockWidth) {
            if (g.getSafe(x, y, PositionType.VOID) == PositionType.VOID) {
                continue
            }
            cubeSides.add(CubeSide(Coord(x, y), g.subGrid(x, y, blockWidth, blockWidth)))
        }
    }
    //   0
    // 123
    //   45
    val neighbours = if (stage == "example") {
        listOf(
            // 0
            SideNeighbours(
                right = Neighbour(5, Direction.LEFT),
                down = Neighbour(3, Direction.DOWN),
                left = Neighbour(2, Direction.DOWN),
                up = Neighbour(1, Direction.DOWN)
            ),
            // 1
            SideNeighbours(
                right = Neighbour(2, Direction.RIGHT),
                down = Neighbour(4, Direction.UP),
                left = Neighbour(5, Direction.UP),
                up = Neighbour(0, Direction.DOWN)
            ),
            // 2
            SideNeighbours(
                right = Neighbour(3, Direction.RIGHT),
                down = Neighbour(4, Direction.RIGHT),
                left = Neighbour(1, Direction.LEFT),
                up = Neighbour(0, Direction.RIGHT)
            ),
            // 3
            SideNeighbours(
                right = Neighbour(5, Direction.DOWN),
                down = Neighbour(4, Direction.DOWN),
                left = Neighbour(2, Direction.LEFT),
                up = Neighbour(0, Direction.UP)
            ),
            // 4
            SideNeighbours(
                right = Neighbour(5, Direction.RIGHT),
                down = Neighbour(1, Direction.UP),
                left = Neighbour(2, Direction.UP),
                up = Neighbour(3, Direction.UP)
            ),
            // 5
            SideNeighbours(
                right = Neighbour(0, Direction.LEFT),
                down = Neighbour(1, Direction.RIGHT),
                left = Neighbour(4, Direction.LEFT),
                up = Neighbour(3, Direction.LEFT)
            )
        )
    }
    //  01
    //  2
    // 34
    // 5
    else {
        listOf(
            // 0
            SideNeighbours(
                right = Neighbour(1, Direction.RIGHT),
                down = Neighbour(2, Direction.DOWN),
                left = Neighbour(3, Direction.RIGHT),
                up = Neighbour(5, Direction.RIGHT)
            ),
            // 1
            SideNeighbours(
                right = Neighbour(4, Direction.LEFT),
                down = Neighbour(2, Direction.LEFT),
                left = Neighbour(0, Direction.LEFT),
                up = Neighbour(5, Direction.UP)
            ),
            // 2
            SideNeighbours(
                right = Neighbour(1, Direction.UP),
                down = Neighbour(4, Direction.DOWN),
                left = Neighbour(3, Direction.DOWN),
                up = Neighbour(0, Direction.UP)
            ),
            // 3
            SideNeighbours(
                right = Neighbour(4, Direction.RIGHT),
                down = Neighbour(5, Direction.DOWN),
                left = Neighbour(0, Direction.RIGHT),
                up = Neighbour(2, Direction.RIGHT)
            ),
            // 4
            SideNeighbours(
                right = Neighbour(1, Direction.LEFT),
                down = Neighbour(5, Direction.LEFT),
                left = Neighbour(3, Direction.LEFT),
                up = Neighbour(2, Direction.UP)
            ),
            // 5
            SideNeighbours(
                right = Neighbour(4, Direction.UP),
                down = Neighbour(1, Direction.DOWN),
                left = Neighbour(0, Direction.DOWN),
                up = Neighbour(3, Direction.UP)
            )
        )
    }
    val cube = Cube(cubeSides, neighbours)

    var curState = CubePosition(0, Coord(0, 0), Direction.RIGHT)
    input.split("\n\n").last().trim().toActions().forEach {
        curState = actionOnCube(curState, it, cube)
        drawCube(curState, cube, g.width(), g.height())
    }

    val finalPos = originalPos(curState, cube)
    val answer = 1000 * (finalPos.y + 1) + 4 * (finalPos.x + 1) + curState.orientation.ordinal
    val want = if (stage == "problem") 184106 else 5031
    printAnswer(answer, want, "Part 2")
}

fun originalPos(cubePos: CubePosition, cube: Cube): Coord {
    return cube.sides[cubePos.side].offset + cubePos.position
}

fun nextPosition(cur: CubePosition, cube: Cube): CubePosition {
    var p = cur.position + when (cur.orientation) {
        Direction.RIGHT -> grid.Right
        Direction.LEFT -> grid.Left
        Direction.DOWN -> grid.Up
        Direction.UP -> grid.Down
    }

    val side = cube.sides[cur.side]
    if (side.grid.contains(p)) {
        return CubePosition(cur.side, p, cur.orientation)
    }

    val neighbours = cube.neighbours[cur.side]
    val w = side.grid.width() - 1
    val h = side.grid.height() - 1
    val px = cur.position.x
    val py = cur.position.y
    when (cur.orientation) {
        Direction.RIGHT -> {
            val n = neighbours.right
            p = when (n.enterDir) {
                Direction.DOWN -> Coord(w - py, 0)
                Direction.UP -> Coord(py, h)
                Direction.RIGHT -> Coord(0, py)
                Direction.LEFT -> Coord(w, h - py)
            }
            return CubePosition(n.index, p, n.enterDir)
        }
        Direction.LEFT -> {
            val n = neighbours.left
            p = when (n.enterDir) {
                Direction.DOWN -> Coord(py, 0)
                Direction.UP -> Coord(w - py, h)
                Direction.RIGHT -> Coord(0, h - py)
                Direction.LEFT -> Coord(w, py)
            }
            return CubePosition(n.index, p, n.enterDir)
        }
        Direction.DOWN -> {
            val n = neighbours.down
            p = when (n.enterDir) {
                Direction.DOWN -> Coord(px, 0)
                Direction.UP -> Coord(w - px, h)
                Direction.RIGHT -> Coord(0, h - px)
                Direction.LEFT -> Coord(w, px)
            }
            return CubePosition(n.index, p, n.enterDir)
        }
        Direction.UP -> {
            val n = neighbours.up
            p = when (n.enterDir) {
                Direction.DOWN -> Coord(w - px, 0)
                Direction.UP -> Coord(px, h)
                Direction.RIGHT -> Coord(0, px)
                Direction.LEFT -> Coord(w, h - px)
            }
            return CubePosition(n.index, p, n.enterDir)
        }
    }
}

fun actionOnCube(state: CubePosition, action: Action, cube: Cube): CubePosition {
    var newState = state
    when (action.type) {
        ActionType.MOVE_FORWARD -> {
            for (i in 0.until(action.value)) {
                val next = nextPosition(newState, cube)
                if (cube.get(next) == PositionType.BLOCKED) {
                    continue
                }
                newState = next
            }
        }

        ActionType.TURN_LEFT -> {
            newState.orientation =
                Direction.values()[(state.orientation.ordinal - 1).mod(Direction.values().size)]
        }
        ActionType.TURN_RIGHT -> {
            newState.orientation =
                Direction.values()[(state.orientation.ordinal + 1).mod(Direction.values().size)]
        }
    }
    return newState
}

fun drawGrid(g: Grid<PositionType>) {
    println(
        g.toStringCustom {
            when (it) {
                PositionType.EMPTY -> "."
                PositionType.BLOCKED -> "#"
                PositionType.POS -> "X"
                else -> " "
            }
        }
    )
}

fun drawCube(pos: CubePosition, cube: Cube, w: Int, h: Int) {
    val pg = grid.NewGrid(w, h, PositionType.VOID).toMutableGrid()
    for (side in cube.sides) {
        side.grid.forEachCell { coord, positionType ->
            pg.set(coord + side.offset, positionType)
        }
    }
    pg.set(originalPos(pos, cube), PositionType.POS)
    drawGrid(pg)
}
