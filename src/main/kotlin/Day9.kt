import grid.*
import kotlin.math.absoluteValue
import kotlin.math.sign

data class Move(val dir: Coord, val amount: Int)

fun String.toMove(): Move {
    val parts = this.trim().split(" ")
    val dir = when (parts.first()) {
        "U" -> Up
        "D" -> Down
        "L" -> Left
        "R" -> Right
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

fun day9(input: String): String {
    val moves = input.split("\n").map(String::toMove)

    // Part 1
    // val pieces = MutableList(2) { Coord(0, 0) }
    // Part 2
    val pieces = MutableList(10) { Coord(0, 0) }
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

    return "${visited.size} (want 2536)"
}
