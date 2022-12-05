typealias Crate = Char
typealias Stack = ArrayList<Crate>
data class Operation(val count: Int, val from: Int, val to: Int)

fun String.toOperation(): Operation {
    val parts = this.split(" ").map(String::toIntOrNull).filterNotNull()
    return Operation(parts[0], parts[1] - 1, parts[2] - 1)
}

fun parse(lines: List<String>): Pair<ArrayList<Stack>, ArrayList<Operation>> {
    var splitLineIndex = 0
    for (i in lines.indices) {
        if (lines[i].isEmpty()) {
            splitLineIndex = i
            break
        }
    }

    val numStacks = lines[splitLineIndex - 1].trim().split("""\s+""".toRegex()).size
    val stacks = ArrayList(List(numStacks) { Stack() })
    for (i in splitLineIndex - 2 downTo 0) {
        lines[i].windowed(3, 4)
            .map { it[1] }
            .withIndex()
            .forEach { if (it.value != ' ') stacks[it.index].add(it.value) }
    }

    return Pair(
        stacks,
        ArrayList(lines.subList(splitLineIndex + 1, lines.size).map { it.toOperation() })
    )
}

fun day5(lines: List<String>): String {
    val (stacks, ops) = parse(lines)

    for (op in ops) {
        // Part 1
//        for (i in 0 until op.count) {
//            stacks[op.to].add(stacks[op.from].last())
//            stacks[op.from].removeAt(stacks[op.from].size - 1)
//        }
        val size = stacks[op.from].size
        stacks[op.to].addAll(stacks[op.from].subList(size - op.count, size))
        stacks[op.from] = Stack(stacks[op.from].subList(0, size - op.count))
    }
    return stacks.fold("") { a, v -> a + v.last() }
}
