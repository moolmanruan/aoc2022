package day21

import output.printAnswer

data class Operation(val left: String, val right: String, val op: String)
data class Monkey(val name: String, var value: Double?, val operation: Operation?)

fun String.toMonkey(): Monkey {
    val parts = this.split(":")
    val name = parts.first()
    val opParts = parts.last().trim().split(" ")
    if (opParts.size == 1) {
        return Monkey(name, opParts.first().toDouble(), null)
    }
    return Monkey(name, null, Operation(opParts.first(), opParts.last(), opParts[1]))
}

fun run(input: String, stage: String) {
    part1(input, stage)
    part2(input, stage)
}

const val ROOT = "root"
const val ME = "humn"

fun part1(input: String, stage: String) {
    val monkeys = input.split("\n").map(String::toMonkey).associateBy { it.name }
    val answer = calculate(monkeys, ROOT)
    val want: Long = if (stage == "problem") 70674280581468 else 152
    printAnswer(answer?.toLong(), want, "Part 1")
}

fun part2(input: String, stage: String) {
    val monkeys = input.split("\n").map(String::toMonkey).associateBy { it.name }.toMutableMap()
    val root = monkeys.remove(ROOT) ?: throw Exception("No root monkey")

    monkeys[ME]!!.value = null
    val op = root.operation!!
    val a = calculate(monkeys, op.left)
    val b = calculate(monkeys, op.right)
    val v = a ?: b ?: throw Exception("left and right are null")
    satisfy(monkeys, if (a == null) op.left else op.right, v)

    val want: Long = if (stage == "problem") 3243420789721 else 301
    printAnswer(monkeys[ME]!!.value!!.toLong(), want, "Part 2")
}

fun calculate(monkeys: Map<String, Monkey>, target: String): Double? {
    val c = monkeys[target] ?: throw Exception("No target monkey")

    if (c.value == null && c.operation == null) {
        return null
    }
    if (c.value != null) {
        return c.value
    }

    val calc = c.operation!!
    val a = calculate(monkeys, calc.left)
    val b = calculate(monkeys, calc.right)
    if (a == null || b == null) {
        return null
    }
    return when (calc.op) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> a / b
        else -> throw Exception("Invalid operation")
    }
}

fun satisfy(monkeys: MutableMap<String, Monkey>, target: String, value: Double) {
    val m = monkeys[target] ?: throw Exception("No target monkey")

//    println("Want $target to equal $value")
    if (m.operation == null) {
        // should be the value given
//        println("Setting ${m.name}'s value to = $value")
        m.value = value
        return
    }

    val op = m.operation
    val a = calculate(monkeys, op.left)
    val b = calculate(monkeys, op.right)
    val v = (a ?: b)!!
//    println("$target: ${op.left} ${op.op} ${op.right} = $value")
//    println("$target: $a ${op.op} $b = $value")
    var newValue = value
    when (op.op) {
        "+" -> newValue -= v
        "*" -> newValue /= v
        "/" -> if (a == null) newValue *= b!! else newValue = a / newValue
        "-" -> if (a == null) newValue += b!! else newValue = a - newValue
        else -> throw Exception("Invalid op")
    }
    satisfy(monkeys, if (a == null) op.left else op.right, newValue)
}
