package day5

import output.printAnswer

typealias Crate = Char
typealias Stack = MutableList<Crate>
data class Operation(val count: Int, val from: Int, val to: Int)

fun String.toOperation(): Operation {
    val parts = this.split(" ").map(String::toIntOrNull).filterNotNull()
    return Operation(parts[0], parts[1] - 1, parts[2] - 1)
}

fun parse(input: String): Pair<List<Stack>, List<Operation>> {
    val inputParts = input.split("\n\n")
    val stackLines = inputParts.first().split("\n").reversed()
    val numStacks = stackLines.first().trim().split("""\s+""".toRegex()).size
    val stacks: List<Stack> = List(numStacks) { ArrayList() }
    for (i in 1 until stackLines.size) {
        stackLines[i].windowed(3, 4)
            .map { it[1] }
            .withIndex()
            .forEach { if (it.value != ' ') stacks[it.index].add(it.value) }
    }
    val ops = inputParts[1].trim().split("\n").map(String::toOperation)
    return Pair(stacks, ops)
}

fun part1(input: String, stage: String) {
    val (stacks, ops) = parse(input)
    for (op in ops) {
        for (i in 0 until op.count) {
            if (stacks[op.from].isNotEmpty()) {
                stacks[op.to].add(stacks[op.from].removeLast())
            }
        }
    }
    val ans = stacks.fold("") { a, v -> a + v.last() }
    val want = if (stage == "problem") "QNHWJVJZW" else "CMZ"
    printAnswer(ans, want, "Part 1")
}

fun part2(input: String, stage: String) {
    val (stacks, ops) = parse(input)
    val ss = stacks.toMutableList()
    for (op in ops) {
        ss[op.to].addAll(ss[op.from].takeLast(op.count))
        ss[op.from] = ss[op.from].dropLast(op.count).toMutableList()
    }
    val ans = ss.fold("") { a, v -> a + v.last() }
    val want = if (stage == "problem") "BPCZJLFJW" else "MCD"
    printAnswer(ans, want, "Part 2")
}

fun run(input: String, stage: String): String {
    part1(input, stage)
    part2(input, stage)
    return ""
}
