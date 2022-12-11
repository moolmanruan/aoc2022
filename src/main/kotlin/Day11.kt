import java.math.BigInteger

data class Monkey(
    val index: Int,
    var items: MutableList<BagItem>,
    val operation: (Int) -> Int,
    val test: (Int) -> Boolean,
    val testVal: Int,
    val testTrue: Int, // monkey to throw to
    val testFalse: Int, // monkey to throw to
    var numInspected: Int
)

fun toMonkey(index: Int, text: String): Monkey {
    val lines = text.split("\n").map(String::trim)
    return Monkey(
        index,
        lines[1].split(":").last()
            .split(",").map { it.trim().toInt() }
            .map { BagItem(it, it) }.toMutableList(),
        lines[2].toOp(),
        lines[3].toTest(),
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
fun String.toTest(): (Int) -> Boolean {
    val value = this.split("divisible by").last().trim().toInt()
    return { v -> (v % value) == 0 }
}

data class BagItem(val init: Int, var worry: Int, val monkeys: MutableMap<Int, Int> = mutableMapOf())

fun playPart1(monkeys: List<Monkey>, rounds: Int): String {
    for (round in 0.until(rounds)) {
        monkeys.forEachIndexed { monkeyIdx, monkey ->
            while (monkey.items.size > 0) {
                val item = monkey.items.first()
                monkey.items = monkey.items.drop(1).toMutableList()
                monkey.numInspected++

                item.worry = monkey.operation(item.worry) / 3

                if (item.worry % monkey.testVal == 0) {
                    monkeys[monkey.testTrue].items.add(item)
                } else {
                    monkeys[monkey.testFalse].items.add(item)
                }
            }
        }
    }

    var answer = BigInteger.valueOf(1)
    for (v in monkeys.map { it.numInspected }.sorted().reversed().take(2)) {
        val bv = BigInteger.valueOf(v.toLong())
        answer = answer.multiply(bv)
    }
    return answer.toString()
}

fun play(monkeys: List<Monkey>, rounds: Int): String {
    for (round in 0.until(rounds)) {
        monkeys.forEachIndexed { monkeyIdx, monkey ->
            while (monkey.items.size > 0) {
                val item = monkey.items.first()
                monkey.items = monkey.items.drop(1).toMutableList()
                monkey.numInspected++

                monkeys.forEachIndexed { i, m ->
                    val newVal = monkey.operation(item.monkeys[i]!!)
                    item.monkeys[i] = newVal % m.testVal
                }

                if (item.monkeys[monkeyIdx] == 0) {
                    monkeys[monkey.testTrue].items.add(item)
                } else {
                    monkeys[monkey.testFalse].items.add(item)
                }
            }
        }
    }

    var answer = BigInteger.valueOf(1)
    for (v in monkeys.map { it.numInspected }.sorted().reversed().take(2)) {
        val bv = BigInteger.valueOf(v.toLong())
        answer = answer.multiply(bv)
    }
    return answer.toString()
}

fun day11(input: String): String {
    val monkeysText = input.split("\n\n")

    val monkeys = monkeysText.mapIndexed { i, v -> toMonkey(i, v) }
    // Initialize items
    val items = monkeys.map { it.items }.fold(mutableListOf<BagItem>()) { a, v -> (a + v).toMutableList() }
    items.forEach { item ->
        monkeys.forEachIndexed { i, m -> item.monkeys[i] = item.init % m.testVal }
    }

//    return "${playPart1(monkeys, 20)} want 113220" // Part 1
    return "${play(monkeys, 10000)} want 30599555965" // Part 2
}
