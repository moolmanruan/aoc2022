package grid

data class Grid<T>(val data: List<List<T>>) {
    fun get(x: Int, y: Int): T {
        return this.data[x][y]
    }
    fun getSafe(x: Int, y: Int, default: T): T {
        return if (x in this.data.indices && y in this.data[x].indices) this.get(x, y) else default
    }
    fun width(): Int {
        return if (this.data.isNotEmpty()) this.data.first().size else 0
    }
    fun height(): Int {
        return this.data.size
    }
}

fun <T> NewGrid(input: String, fn: (String) -> T): Grid<T> {
    val data = input.split("\n").map {
        it.split(",").map(fn)
    }
    return Grid(data)
}
