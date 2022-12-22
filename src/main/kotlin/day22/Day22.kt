package day22

import grid.Coord
import grid.plus
import output.printAnswer

enum class PositionType { VOID, EMPTY, BLOCKED }

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
//    println(
//        g.toStringCustom {
//            when (it) {
//                PositionType.EMPTY -> "."
//                PositionType.BLOCKED -> "#"
//                else -> " "
//            }
//        }
//    )
//    println(moves)
    val x = 0.until(g.width()).find { g.get(Coord(it, 0)) == PositionType.EMPTY } ?: throw Exception("Couldn't find start")
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
                curState.orientation = Direction.values()[(curState.orientation.ordinal - 1).mod(Direction.values().size)]
            }
            ActionType.TURN_RIGHT -> {
                curState.orientation = Direction.values()[(curState.orientation.ordinal + 1).mod(Direction.values().size)]
            }
        }
    }

    val answer = 1000 * (curState.position.y + 1) + 4 * (curState.position.x + 1) + curState.orientation.ordinal
    val want = if (stage == "problem") 30552 else 6032
    printAnswer(answer, want, "Part 1")
}
