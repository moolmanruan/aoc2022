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

fun BlizzardState.asStringImage(me: Coord? = null): String {
    val blockSize = 5
    val g = grid.NewGrid((size.x + 2) * blockSize, (size.y + 2) * blockSize, "e").toMutableGrid()
    val xm = size.x + 1
    val ym = size.y + 1
    fun drawWall(x: Int, y: Int) {
        (x * blockSize until (x + 1) * blockSize).forEach { x ->
            (y * blockSize until (y + 1) * blockSize).forEach { y ->
                g.set(x, y, "w")
            }
        }
    }
    fun drawBlizzard(x: Int, y: Int, d: Direction) {
        val xs = (x * blockSize) + 2
        val ys = (y * blockSize) + 2
        (xs - 1..xs + 1).forEach { x ->
            (ys - 1..ys + 1).forEach { y ->
                g.set(x, y, "x")
            }
        }
        when (d) {
            Direction.UP -> {
                (xs - 1..xs + 1).forEach { x ->
                    g.set(x, ys - 2, "u")
                }
            }
            Direction.DOWN -> {
                (xs - 1..xs + 1).forEach { x ->
                    g.set(x, ys + 2, "d")
                }
            }
            Direction.RIGHT -> {
                (ys - 1..ys + 1).forEach { y ->
                    g.set(xs + 2, y, "r")
                }
            }
            Direction.LEFT -> {
                (ys - 1..ys + 1).forEach { y ->
                    g.set(xs - 2, y, "l")
                }
            }
        }
    }
    fun drawParty(x: Int, y: Int) {
        val xs = (x * blockSize) + 2
        val ys = (y * blockSize) + 2
        (xs - 1..xs + 1).forEach { x ->
            (ys - 1..ys + 1).forEach { y ->
                g.set(x, y, "o")
            }
        }
    }

    val startPos = Coord(1, 0)
    val endPos = size + grid.Up
    for (x in 0..xm) {
        for (y in 0..ym) {
            val c = Coord(x, y)
            if ((y == 0 || x == 0 || y == ym || x == xm) &&
                c != startPos &&
                c != endPos
            ) {
                drawWall(x, y)
            }
            if (me == c) {
                drawParty(x, y)
            }

            bb.filter { it.position == c }.forEach {
                drawBlizzard(x, y, it.direction)
            }
        }
    }
    // Make the cells comma-separated
    g.forEachCell { coord, s ->
        if (coord.x < g.width() - 1) {
            g.set(coord, "$s,")
        }
    }
    return g.toString()
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
    part1(input, stage)
    part2(input, stage)
}

data class Position(val current: Coord, val prev: Position?)

data class Result(val rounds: Int, val position: Position, val blizzard: BlizzardState)
fun findPath(start: Coord, destination: Coord, startState: BlizzardState): Result {
    var round = 0
    val startPos = Position(start, null)
    var positions = listOf(startPos)
    var blizzard = startState

    while (true) {
        round++
        blizzard = blizzard.next()

        val newPositions = mutableMapOf<Coord, Position>()
        positions.forEach { p ->
            nextStates(p.current, blizzard).map { c ->
                if (c !in newPositions) {
                    newPositions[c] = Position(c, p)
                }
            }
        }
        if (destination in newPositions) {
            return Result(round, newPositions[destination]!!, blizzard)
        }
        positions = newPositions.map { (_, v) -> v }.sortedBy { it.current.l1Distance(blizzard.size) }
    }
}

val startPos = Coord(1, 0)
fun part2(input: String, stage: String) {
    val blizzard = input.toState()
    val endPos = blizzard.size + grid.Up

    val there = findPath(startPos, endPos, blizzard)
    printAnswer(there.rounds, if (stage == "problem") 274 else 18, "There")
    val back = findPath(endPos, startPos, there.blizzard)
    printAnswer(back.rounds, if (stage == "problem") 294 else 23, "Back")
    val thereAgain = findPath(startPos, endPos, back.blizzard)
    printAnswer(thereAgain.rounds, if (stage == "problem") 271 else 13, "There Again")

    val want = if (stage == "problem") 274 + 294 + 271 else 18 + 23 + 13
    printAnswer(there.rounds + back.rounds + thereAgain.rounds, want, "Part 1")
}

fun part1(input: String, stage: String) {
    val blizzard = input.toState()
    val endPos = blizzard.size + grid.Up
    val result = findPath(startPos, endPos, blizzard)

    // Output gif text file
//    val f = File("/Users/ruan/aoc2022/src/main/kotlin/day24/output.txt")
//    f.writeText(
//        """
//        delay:10
//        scale:1
//        ---
//        w:C4,A4,84,ff
//        e:ff,ff,ff,ff
//        o:00,00,00,ff
//        x:8E,F0,EF,ff
//        r:8E,A7,F0,ff
//        l:8E,A7,F0,ff
//        u:8E,A7,F0,ff
//        d:8E,A7,F0,ff
//        ---
//        """.trimIndent()
//    )
//    var b = input.toState()
//    val path = mutableListOf<Coord>()
//    var p: Position? = result.position
//    while (p != null) {
//        path.add(p.current)
//        p = p.prev
//    }
//    path.reverse()
//    f.appendText("-\n${b.asStringImage(path[0])}")
//    repeat(result.rounds) {
//        b = b.next()
//        f.appendText("-\n${b.asStringImage(path[it + 1])}")
//    }

    val want = if (stage == "problem") 274 else 18
    printAnswer(result.rounds, want, "Part 1")
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
