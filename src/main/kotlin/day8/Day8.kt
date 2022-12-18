package day8

import grid.plus
import output.printAnswer

fun visible(g: grid.Grid<Int>, c: grid.Coord, dir: grid.Coord): Boolean {
    val v = g.get(c.x, c.y)
    var other = c + dir
    while (g.contains(other)) {
        if (g.get(other.x, other.y) >= v) {
            return false
        }
        other += dir
    }
    return true
}

fun numVisible(g: grid.Grid<Int>, c: grid.Coord, dir: grid.Coord): Int {
    var count = 0
    val v = g.get(c.x, c.y)
    var other = c + dir
    while (g.contains(other)) {
        count++
        if (g.get(other.x, other.y) >= v) {
            return count
        }
        other += dir
    }
    return count
}

fun run(input: String, stage: String): String {
    val g = grid.NewGridFromString(input, "") { it.toInt() }

    // part one
    val vis = grid.NewGrid(g.width(), g.height(), false).toMutableGrid()
    g.forEachCell { c, _ ->
        vis.set(
            c.x,
            c.y,
            visible(g, c, grid.Up) ||
                visible(g, c, grid.Down) ||
                visible(g, c, grid.Left) ||
                visible(g, c, grid.Right)
        )
    }

    var sum = 0
    vis.forEach { if (it) sum++ }
    val want1 = if (stage == "problem") 1816 else 21
    printAnswer(sum, want1, "Part 1")

    // part two
    val scenic = grid.NewGrid(g.width(), g.height(), 0).toMutableGrid()
    g.forEachCell { c, v ->
        scenic.set(
            c.x,
            c.y,
            numVisible(g, c, grid.Up) *
                numVisible(g, c, grid.Down) *
                numVisible(g, c, grid.Left) *
                numVisible(g, c, grid.Right)
        )
    }
    var best = 0
    scenic.forEach { if (it > best) best = it }
    val want2 = if (stage == "problem") 383520 else 8
    printAnswer(best, want2, "Part 2")
    return ""
}
