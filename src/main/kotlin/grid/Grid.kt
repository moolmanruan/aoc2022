package grid

data class Coord(val x: Int, val y: Int)

val Up = Coord(0, 1)
val Down = Coord(0, -1)
val Left = Coord(-1, 0)
val Right = Coord(1, 0)

fun Coord.add(other: Coord): Coord {
    return Coord(this.x + other.x, this.y + other.y)
}

fun Coord.copy(other: Coord): Coord {
    return Coord(other.x, other.y)
}

open class Grid<T>(private val data: List<List<T>>) {
    fun get(x: Int, y: Int): T {
        return this.data[y][x]
    }
    fun getSafe(x: Int, y: Int, default: T): T {
        return if (y in this.data.indices && x in this.data[y].indices) this.get(x, y) else default
    }
    fun width(): Int {
        return if (this.data.isNotEmpty()) this.data.first().size else 0
    }
    fun height(): Int {
        return this.data.size
    }

    fun contains(c: Coord): Boolean {
        return (c.y in this.data.indices && c.x in this.data[c.y].indices)
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
            lines.add(row.joinToString(","))
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

class MutableGrid<T>(private val data: List<MutableList<T>>) : Grid<T>(data) {
    fun set(x: Int, y: Int, value: T) {
        this.data[y][x] = value
    }
    fun setSafe(x: Int, y: Int, value: T) {
        if (y in this.data.indices && x in this.data[y].indices) this.set(x, y, value)
    }
}
