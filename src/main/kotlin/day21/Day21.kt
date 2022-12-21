package day21

import output.printAnswer

data class Operation(val left: String, val right: String, val op: String)
data class Monkey(val name: String, var value: Long?, val operation: Operation?)

fun String.toMonkey(): Monkey {
    val parts = this.split(":")
    val name = parts.first()
    val opParts = parts.last().trim().split(" ")
    if (opParts.size == 1) {
        return Monkey(name, opParts.first().toLong(), null)
    }
    return Monkey(name, null, Operation(opParts.first(), opParts.last(), opParts[1]))
}

fun run(input: String, stage: String) {
    part1(input, stage)
}

fun part1(input: String, stage: String) {
    val monkeys = input.split("\n").map(String::toMonkey).associateBy { it.name }
    val root = monkeys["root"] ?: throw Exception("No root monkey")
    while (root.value == null) {
        monkeys.forEach { name, mon ->
            if (mon.operation != null) {
                val a = monkeys[mon.operation.left] ?: throw Exception("Monkey not found")
                val b = monkeys[mon.operation.right] ?: throw Exception("Monkey not found")
                if (a.value != null && b.value != null) {
                    mon.value = when (mon.operation.op) {
                        "+" -> a.value!! + b.value!!
                        "-" -> a.value!! - b.value!!
                        "*" -> a.value!! * b.value!!
                        "/" -> a.value!! / b.value!!
                        else -> throw Exception("Invalid operation")
                    }
                }
            }
        }
    }

    val want: Long = if (stage == "problem") 70674280581468 else 152
    printAnswer(root.value, want, "Part 1")
}
