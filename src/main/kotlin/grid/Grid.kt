package grid

import kotlin.math.absoluteValue

data class Coord(val x: Int, val y: Int)

val Nothing = Coord(0, 0)

val Up = Coord(0, 1)
val Down = Coord(0, -1)
val Left = Coord(-1, 0)
val Right = Coord(1, 0)

val UpLeft = Coord(-1, 1)
val UpRight = Coord(1, 1)
val DownLeft = Coord(-1, -1)
val DownRight = Coord(1, -1)

operator fun Coord.plus(other: Coord): Coord {
    return Coord(this.x + other.x, this.y + other.y)
}
operator fun Coord.minus(other: Coord): Coord {
    return Coord(this.x - other.x, this.y - other.y)
}

fun Coord.copy(other: Coord): Coord {
    return Coord(other.x, other.y)
}
fun Coord.distanceL1(other: Coord): Int {
    val diff = other - this
    return diff.x + diff.y
}
fun Coord.l1Distance(other: Coord): Int {
    return (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue
}

open class Grid<T>(private val data: List<List<T>>) {

    private var cachedWidth = -1

    fun get(c: Coord): T {
        return get(c.x, c.y)
    }
    fun getSafe(c: Coord, default: T): T {
        return getSafe(c.x, c.y, default)
    }
    fun getWrapped(c: Coord): T {
        return get(c.x.mod(width()), c.y.mod(height()))
    }
    fun getWrappedSafe(c: Coord, default: T): T {
        return getSafe(c.x.mod(width()), c.y.mod(height()), default)
    }
    fun get(x: Int, y: Int): T {
        return this.data[y][x]
    }
    fun getSafe(x: Int, y: Int, default: T): T {
        return if (contains(x, y)) this.get(x, y) else default
    }
    fun width(): Int {
        if (cachedWidth < 0) {
            cachedWidth = data.maxOf { it.size }
        }
        return cachedWidth
    }
    fun height(): Int {
        return this.data.size
    }

    fun contains(x: Int, y: Int): Boolean {
        return (y in this.data.indices && x in this.data[y].indices)
    }
    fun contains(c: Coord): Boolean {
        return contains(c.x, c.y)
    }

    fun forEach(fn: (T) -> Unit) {
        this.data.forEach { it.forEach(fn) }
    }
    fun forEachCell(fn: (Coord, T) -> Unit) {
        this.data.forEachIndexed { y, row -> row.forEachIndexed { x, cell -> fn(Coord(x, y), cell) } }
    }

    override fun toString(): String {
        val lines = mutableListOf<String>()
        for (row in this.data) {
            lines.add(row.joinToString(""))
        }
        return lines.joinToString("\n")
    }

    fun toStringCustom(fn: (T) -> String): String {
        val lines = mutableListOf<String>()
        for (row in this.data) {
            lines.add(row.map(fn).joinToString(""))
        }
        return lines.joinToString("\n")
    }

    fun toMutableGrid(): MutableGrid<T> {
        val newData = mutableListOf<MutableList<T>>()
        for (row in this.data) {
            newData.add(row.toMutableList())
        }
        return MutableGrid(newData)
    }

    fun subGrid(x: Int, y: Int, w: Int, h: Int): MutableGrid<T> {
        val g = NewGrid(w, h, get(Coord(0, 0))).toMutableGrid()
        for (xi in 0.until(w)) {
            for (yi in 0.until(h)) {
                g.set(Coord(xi, yi), get(x + xi, y + yi))
            }
        }
        return g
    }
}

fun <T> NewGrid(width: Int, height: Int, default: T): Grid<T> {
    val data = mutableListOf<List<T>>()
    for (y in 0.until(height).toList()) {
        data.add(List(width) { default })
    }
    return Grid(data)
}

// NewGridFromString creates a grid from a string input, treating each line as a row and using the `separator` to split
// lines into cells. If the separator is empty each character will represent a cell.
// The `fn` can be used to map the string value to the type that grid cells should contain.
fun <T> NewGridFromString(input: String, separator: String, fn: (String) -> T): Grid<T> {
    val data = input.split("\n").map {
        if (separator.isEmpty()) it.windowed(1).map(fn) else it.split(",").map(fn)
    }
    return Grid(data)
}

fun <T> NewGridFromStringIndexed(input: String, separator: String, fn: (String, Coord) -> T): Grid<T> {
    val data = input.split("\n").mapIndexed { y, row ->
        if (separator.isEmpty()) {
            row.windowed(1).mapIndexed { x, cell -> fn(cell, Coord(x, y)) }
        } else {
            row.split(",").mapIndexed { x, cell -> fn(cell, Coord(x, y)) }
        }
    }
    return Grid(data)
}

class MutableGrid<T>(private val data: List<MutableList<T>>) : Grid<T>(data) {
    fun set(c: Coord, value: T) {
        set(c.x, c.y, value)
    }
    fun setSafe(c: Coord, value: T) {
        setSafe(c.x, c.y, value)
    }
    fun set(x: Int, y: Int, value: T) {
        this.data[y][x] = value
    }
    fun setSafe(x: Int, y: Int, value: T) {
        if (y in this.data.indices && x in this.data[y].indices) this.set(x, y, value)
    }
}
