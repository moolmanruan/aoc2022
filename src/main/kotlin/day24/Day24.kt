package day24

import grid.Coord
import grid.l1Distance
import grid.plus
import output.printAnswer

enum class Direction { UP, DOWN, LEFT, RIGHT }
data class Blizzard(
    val direction: Direction,
    val position: Coord
) {
    override fun toString(): String {
        return when (direction) {
            Direction.UP -> "^"
            Direction.DOWN -> "v"
            Direction.LEFT -> "<"
            Direction.RIGHT -> ">"
        }
    }
}

fun BlizzardState.next(): BlizzardState {
    return BlizzardState(
        size,
        bb.map { b ->
            var p = b.position
            p = when (b.direction) {
                Direction.UP -> Coord(p.x, if (p.y > 1) p.y - 1 else size.y)
                Direction.DOWN -> Coord(p.x, if (p.y < size.y) p.y + 1 else 1)
                Direction.LEFT -> Coord(if (p.x > 1) p.x - 1 else size.x, p.y)
                Direction.RIGHT -> Coord(if (p.x < size.x) p.x + 1 else 1, p.y)
            }
            Blizzard(b.direction, p)
        }
    )
}

data class BlizzardState(
    val size: Coord,
    val bb: List<Blizzard>
) {
    fun asString(me: Coord? = null): String {
        val lines = mutableListOf<String>()
        val xm = size.x + 1
        val ym = size.y + 1
        for (y in 0..ym) {
            var line = ""
            for (x in 0..xm) {
                if (y == 0 || x == 0 || y == ym || x == xm) {
                    line += "#"
                    continue
                }
                val c = Coord(x, y)
                if (me == c) {
                    line += "E"
                    continue
                }
                val blizCount = bb.count { it.position == c }
                if (blizCount >= 10) {
                    line += "X"
                    continue
                } else if (blizCount > 1) {
                    line += blizCount.toString()
                    continue
                }

                val bliz = bb.find { it.position == c }
                if (bliz != null) {
                    line += bliz.toString()
                    continue
                }
                line += "."
            }
            lines.add(line)
        }
        return lines.joinToString("\n")
    }
}

fun String.toState(): BlizzardState {
    val lines = this.split("\n")
    val blizzard = mutableListOf<Blizzard>()
    lines.forEachIndexed { y, row ->
        row.windowed(1).forEachIndexed { x, cell ->
            val c = Coord(x, y)
            when (cell) {
                ">" -> blizzard.add(Blizzard(Direction.RIGHT, c))
                "<" -> blizzard.add(Blizzard(Direction.LEFT, c))
                "^" -> blizzard.add(Blizzard(Direction.UP, c))
                "v" -> blizzard.add(Blizzard(Direction.DOWN, c))
                else -> {}
            }
        }
    }
    val size = Coord(
        lines[0].length - 2,
        lines.size - 2
    )
    return BlizzardState(size, blizzard)
}

fun run(input: String, stage: String) {
//    part1(input, stage)
    part2(input, stage)
}

fun findPath(start: Coord, destination: Coord, startState: BlizzardState): Pair<Int, BlizzardState> {
    var round = 0
    var positions = listOf(start)
    var blizzard = startState

    while (true) {
        round++
        blizzard = blizzard.next()
        val newPositions = mutableSetOf<Coord>()
        positions.forEach { p ->
            newPositions.addAll(nextStates(p, blizzard))
        }
        if (destination in newPositions) {
            break
        }
        positions = newPositions.sortedBy { it.l1Distance(blizzard.size) }
    }
    return Pair(round, blizzard)
}

val startPos = Coord(1, 0)
fun part2(input: String, stage: String) {
    val blizzard = input.toState()
    val endPos = blizzard.size + grid.Up

    val there = findPath(startPos, endPos, blizzard)
    printAnswer(there.first, if (stage == "problem") 274 else 18, "There")
    val back = findPath(endPos, startPos, there.second)
    printAnswer(back.first, if (stage == "problem") 294 else 23, "Back")
    val thereAgain = findPath(startPos, endPos, back.second)
    printAnswer(thereAgain.first, if (stage == "problem") 271 else 13, "There Again")

    val want = if (stage == "problem") 274 + 294 + 271 else 18 + 23 + 13
    printAnswer(there.first + back.first + thereAgain.first, want, "Part 1")
}

fun part1(input: String, stage: String) {
    val blizzard = input.toState()
    val endPos = blizzard.size + grid.Up
    val rounds = findPath(startPos, endPos, blizzard)

    val want = if (stage == "problem") 274 else 18
    printAnswer(rounds, want, "Part 1")
}

fun nextStates(current: Coord, blizzard: BlizzardState): List<Coord> {
    val next = mutableListOf<Coord>()
    val blizzardPositions = blizzard.bb.map { it.position }.toSet()
    val endPos = blizzard.size + grid.Up
    fun inBounds(c: Coord): Boolean = c.x > 0 && c.y > 0 && c.x <= blizzard.size.x && c.y <= blizzard.size.y
    listOf(grid.Right, grid.Up, grid.Nothing, grid.Left, grid.Down).forEach { offset ->
        val n = current + offset
        if ((inBounds(n) || n == startPos || n == endPos) && n !in blizzardPositions) {
            next.add(n)
        }
    }
    return next
}
