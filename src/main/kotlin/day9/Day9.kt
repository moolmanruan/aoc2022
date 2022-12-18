package day9

import grid.Coord
import grid.plus
import output.printAnswer
import kotlin.math.absoluteValue
import kotlin.math.sign

data class Move(val dir: Coord, val amount: Int)

fun String.toMove(): Move {
    val parts = this.trim().split(" ")
    val dir = when (parts.first()) {
        "U" -> grid.Up
        "D" -> grid.Down
        "L" -> grid.Left
        "R" -> grid.Right
        else -> Coord(0, 0)
    }
    return Move(dir, parts.last().toInt())
}

fun tailNextPos(headPos: Coord, tailPos: Coord): Coord {
    val diffX = headPos.x - tailPos.x
    val diffY = headPos.y - tailPos.y
    if (diffX.absoluteValue <= 1 && diffY.absoluteValue <= 1) {
        return tailPos.copy()
    }
    return tailPos + Coord(diffX.sign, diffY.sign)
}

fun simulate(pieces: MutableList<Coord>, moves: List<Move>): Int {
    val visited = mutableSetOf<Coord>()
    for (move in moves) {
        for (i in 0.until(move.amount)) {
            for (pi in pieces.indices) {
                if (pi == 0) {
                    pieces[0] += move.dir
                } else {
                    pieces[pi] = tailNextPos(pieces[pi - 1], pieces[pi])
                }
            }
            visited.add(pieces.last())
        }
    }
    return visited.size
}

fun run(input: String, stage: String) {
    val moves = input.split("\n").map(String::toMove)

    // Part 1
    val pieces1 = MutableList(2) { Coord(0, 0) }
    val want1 = if (stage == "problem") 6367 else 13
    printAnswer(simulate(pieces1, moves), want1, "Part 1")
    // Part 2
    val pieces2 = MutableList(10) { Coord(0, 0) }
    val want2 = if (stage == "problem") 2536 else 1
    printAnswer(simulate(pieces2, moves), want2, "Part 2")
}
