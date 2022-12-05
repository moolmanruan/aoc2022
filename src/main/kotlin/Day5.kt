

typealias Crate = Char
typealias Stack = MutableList<Crate>
data class Operation(val count: Int, val from: Int, val to: Int)

fun String.toOperation(): Operation {
    val parts = this.split(" ").map(String::toIntOrNull).filterNotNull()
    return Operation(parts[0], parts[1] - 1, parts[2] - 1)
}

fun parse(lines: List<String>): Pair<List<Stack>, List<Operation>> {
    var splitLineIndex = 0
    for (i in lines.indices) {
        if (lines[i].isEmpty()) {
            splitLineIndex = i
            break
        }
    }

    val numStacks = lines[splitLineIndex - 1].trim().split("""\s+""".toRegex()).size
    val stacks: List<Stack> = List(numStacks) { ArrayList() }
    for (i in splitLineIndex - 2 downTo 0) {
        lines[i].windowed(3, 4)
            .map { it[1] }
            .withIndex()
            .forEach { if (it.value != ' ') stacks[it.index].add(it.value) }
    }
    return Pair(
        stacks,
        lines.subList(splitLineIndex + 1, lines.size).map { it.toOperation() }
    )
}

fun process1(stacks: List<Stack>, ops: List<Operation>): List<Stack> {
    for (op in ops) {
        for (i in 0 until op.count) {
            stacks[op.to].add(stacks[op.from].last())
            stacks[op.from].removeLast()
        }
    }
    return stacks
}

fun process2(stacks: List<Stack>, ops: List<Operation>): List<Stack> {
    val ss = stacks.toMutableList()
    for (op in ops) {
        ss[op.to].addAll(ss[op.from].takeLast(op.count))
        ss[op.from] = ss[op.from].dropLast(op.count).toMutableList()
    }
    return ss
}

fun day5(lines: List<String>): String {
    val (stacks, ops) = parse(lines)
    val ss = process2(stacks, ops)
    return ss.fold("") { a, v -> a + v.last() }
}
