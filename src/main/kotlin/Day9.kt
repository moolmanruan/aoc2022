import grid.*
import kotlin.math.absoluteValue

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
    val moveX = if (diffX > 0) 1 else -1
    val moveY = if (diffY > 0) 1 else -1
    return if (diffX != 0 && diffY != 0) {
        tailPos.add(Coord(moveX, moveY))
    } else if (diffX == 0) {
        tailPos.add(Coord(0, moveY))
    } else {
        tailPos.add(Coord(moveX, 0))
    }
}

fun day9(input: String): String {
    val moves = input.split("\n").map(String::toMove)
    println(moves)

    var headPos = Coord(0, 0)
    var tailPos = Coord(0, 0)
    val visited = mutableSetOf<Coord>()
    for (move in moves) {
        for (i in 0.until(move.amount)) {
            headPos = headPos.add(move.dir)
            tailPos = tailNextPos(headPos, tailPos)
            visited.add(tailPos)
        }
    }

    return visited.size.toString()
}
