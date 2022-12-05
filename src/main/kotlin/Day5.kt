

typealias Crate = Char
typealias Stack = MutableList<Crate>
data class Operation(val count: Int, val from: Int, val to: Int)

fun String.toOperation(): Operation {
    val parts = this.split(" ").map(String::toIntOrNull).filterNotNull()
    return Operation(parts[0], parts[1] - 1, parts[2] - 1)
}

fun parse(input: String): Pair<List<Stack>, List<Operation>> {
    val inputParts = input.split("\n\n")

    val stackLines = inputParts[0].split("\n").reversed()
    val numStacks = stackLines[0].trim().split("""\s+""".toRegex()).size
    val stacks: List<Stack> = List(numStacks) { ArrayList() }
    for (i in 1 until stackLines.size) {
        stackLines[i].windowed(3, 4)
            .map { it[1] }
            .withIndex()
            .forEach { if (it.value != ' ') stacks[it.index].add(it.value) }
    }
    return Pair(
        stacks,
        inputParts[1].split("\n").map { it.toOperation() }
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

fun day5(input: String): String {
    val (stacks, ops) = parse(input)
    val ss = process2(stacks, ops)
    return ss.fold("") { a, v -> a + v.last() }
}
