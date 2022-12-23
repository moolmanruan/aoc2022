package day23

import grid.Coord
import grid.plus
import output.printAnswer

fun run(input: String, stage: String) {
    val elves = mutableListOf<Elf>()
    input.split("\n").forEachIndexed { y, row ->
        row.windowed(1).forEachIndexed { x, v ->
            if (v == "#") {
                elves.add(Coord(x, -y))
            }
        }
    }
    part1(elves, stage)
    part2(elves, stage)
}

typealias Elf = Coord

fun part1(inputElves: List<Elf>, stage: String) {
    var elves = inputElves.toList()
    // Move the elves around for 10 round
    for (round in 0 until 10) {
        elves = tickElves(elves, round)
    }

    val w = elves.maxOf { it.x } - elves.minOf { it.x } + 1
    val h = elves.maxOf { it.y } - elves.minOf { it.y } + 1
    val ans = w * h - elves.size
    val want = if (stage == "problem") 3970 else 110
    printAnswer(ans, want, "Part 1")
}

fun part2(inputElves: List<Elf>, stage: String) {
    var elves = inputElves.toList()
    var prevElves = emptyList<Elf>()
    var round = 0
    // Check at what round the elves stop moving
    while (prevElves != elves) {
        prevElves = elves.toList()
        elves = tickElves(elves, round)
        round++
    }

    val want = if (stage == "problem") 923 else 20
    printAnswer(round, want, "Part 2")
}

val north = listOf(grid.Up, grid.UpLeft, grid.UpRight)
val south = listOf(grid.Down, grid.DownLeft, grid.DownRight)
val west = listOf(grid.Left, grid.UpLeft, grid.DownLeft)
val east = listOf(grid.Right, grid.UpRight, grid.DownRight)

val proposalDirs = listOf(north, south, west, east)
val surroundingPositions = listOf(
    grid.UpLeft,
    grid.Up,
    grid.UpRight,
    grid.Left,
    grid.Right,
    grid.DownLeft,
    grid.Down,
    grid.DownRight
)

fun tickElves(elves: List<Elf>, round: Int): List<Elf> {
    return filterClash(elves, nextMoves(elves, round))
}

fun filterClash(elves: List<Elf>, proposedMoves: List<Elf>): List<Elf> {
    val moveGroups = proposedMoves.groupBy { it }
    return proposedMoves.mapIndexed { index, newElf ->
        if (moveGroups[newElf]!!.size > 1) {
            elves[index]
        } else {
            newElf
        }
    }
}

fun nextMoves(elves: List<Elf>, round: Int): List<Elf> {
    val dirSets = round.until(round + 4).map { proposalDirs[it.mod(4)] }
    val elfPositions = elves.associateBy { it }
    return elves.map { elf ->
        var dir = elf
        if (surroundingPositions.any { (elf + it) in elfPositions }) {
            for (dirSet in dirSets) {
                if (dirSet.none { d -> (elf + d) in elfPositions }) {
                    dir = elf + dirSet[0]
                    break
                }
            }
        }
        dir
    }
}

fun drawElvesToFit(elves: List<Elf>) {
    val minX = elves.minOf { it.x }
    val maxX = elves.maxOf { it.x }
    val minY = elves.minOf { it.y }
    val maxY = elves.maxOf { it.y }
    drawElves(elves, minX, maxX, minY, maxY)
}

fun drawElves(elves: List<Elf>, minX: Int, maxX: Int, minY: Int, maxY: Int) {
    val elfMap = elves.associateBy { it }
    for (y in maxY downTo minY) {
        for (x in minX..maxX) {
            if (Coord(x, y) in elfMap) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}
