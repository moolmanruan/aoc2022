package day11

import output.printAnswer
import java.math.BigInteger

data class Monkey(
    val index: Int,
    var items: MutableList<BagItem>,
    val operation: (Int) -> Int,
    val testValue: Int,
    val throwToTestTrue: Int, // Index of other monkey
    val throwToTestFalse: Int, // Index of other monkey
    var numInspected: Int
)

fun Monkey.inspectItem(): BagItem {
    val item = this.items.first()
    this.items = this.items.drop(1).toMutableList()
    this.numInspected++
    return item
}

fun Monkey.clone(): Monkey {
    return Monkey(
        this.index,
        this.items.map { it.clone() }.toMutableList(),
        this.operation,
        this.testValue,
        this.throwToTestTrue,
        this.throwToTestFalse,
        this.numInspected
    )
}

fun toMonkey(index: Int, text: String): Monkey {
    val lines = text.split("\n").map(String::trim)
    return Monkey(
        index,
        lines[1].split(":").last()
            .split(",").map { it.trim().toInt() }
            .map { BagItem(it, it) }.toMutableList(),
        lines[2].toOp(),
        lines[3].split(" ").last().toInt(),
        lines[4].split(" ").last().toInt(),
        lines[5].split(" ").last().toInt(),
        0
    )
}

fun String.toOp(): (Int) -> Int {
    val parts = this.split("=").last().trim().split(" ")
    return when (parts[1]) {
        "*" -> { v -> v * if (parts.last() == "old") v else parts.last().toInt() }
        "+" -> { v -> v + if (parts.last() == "old") v else parts.last().toInt() }
        else -> throw Exception("unsupported operation")
    }
}

data class BagItem(val init: Int, var worry: Int, val monkeys: MutableMap<Int, Int> = mutableMapOf())

fun BagItem.clone(): BagItem {
    return BagItem(
        this.init,
        this.worry,
        this.monkeys.toMutableMap()
    )
}

fun inspectItemPart1(monkey: Monkey, monkeys: List<Monkey>) {
    val item = monkey.inspectItem()

    item.worry = monkey.operation(item.worry) / 3

    if (item.worry % monkey.testValue == 0) {
        monkeys[monkey.throwToTestTrue].items.add(item)
    } else {
        monkeys[monkey.throwToTestFalse].items.add(item)
    }
}

fun inspectItemPart2(monkey: Monkey, monkeys: List<Monkey>) {
    val item = monkey.inspectItem()

    monkeys.forEachIndexed { i, m ->
        val newVal = monkey.operation(item.monkeys[i]!!)
        item.monkeys[i] = newVal % m.testValue
    }

    if (item.monkeys[monkey.index] == 0) {
        monkeys[monkey.throwToTestTrue].items.add(item)
    } else {
        monkeys[monkey.throwToTestFalse].items.add(item)
    }
}

fun play(monkeys: List<Monkey>, rounds: Int, inspectFn: (monkey: Monkey, monkeys: List<Monkey>) -> Unit): String {
    for (round in 0.until(rounds)) {
        monkeys.forEach {
            while (it.items.size > 0) {
                inspectFn(it, monkeys)
            }
        }
    }

    return monkeys.map { it.numInspected }
        .sorted().reversed().take(2)
        .fold(BigInteger.valueOf(1)) { a, v -> a.multiply(BigInteger.valueOf(v.toLong())) }
        .toString()
}

fun run(input: String, stage: String): String {
    val monkeysText = input.split("\n\n")

    val monkeys = monkeysText.mapIndexed { i, v -> toMonkey(i, v) }
    // Initialize items
    val items = monkeys.map { it.items }.fold(mutableListOf<BagItem>()) { a, v -> (a + v).toMutableList() }
    items.forEach { item ->
        monkeys.forEachIndexed { i, m -> item.monkeys[i] = item.init % m.testValue }
    }

    val ans1 = play(monkeys.map { it.clone() }, 20, ::inspectItemPart1)
    val want1 = if (stage == "problem") 113220 else 10605
    printAnswer(ans1, want1, "Part 1")

    val ans2 = play(monkeys.map { it.clone() }, 10000, ::inspectItemPart2)
    val want2 = if (stage == "problem") 30599555965 else 2713310158
    printAnswer(ans2, want2, "Part 2")
    return ""
}
