

data class Monkey(
    var items: MutableList<Int>,
    val operation: (Int) -> Int,
    val test: (Int) -> Boolean,
    val testTrue: Int, // monkey to throw to
    val testFalse: Int, // monkey to throw to
    var numInspected: Int
)

fun String.toMonkey(): Monkey {
    val lines = this.split("\n").map(String::trim)
    return Monkey(
        lines[1].split(":").last()
            .split(",").map { it.trim().toInt() }.toMutableList(),
        lines[2].toOp(),
        lines[3].toTest(),
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

fun day11(input: String): String {
    val monkeys = input.split("\n\n").map(String::toMonkey)

    for (round in 0.until(20)) {
        for (monkey in monkeys) {
            while (monkey.items.size > 0) {
                var item = monkey.items.first()
                monkey.items = monkey.items.drop(1).toMutableList()
                monkey.numInspected++

                item = monkey.operation(item) / 3
                if (monkey.test(item)) {
                    monkeys[monkey.testTrue].items.add(item)
                } else {
                    monkeys[monkey.testFalse].items.add(item)
                }
            }
        }
//        var c = 0
//        for (m in monkeys) {
//            println("Monkey $c: ${m.items}")
//            c++
//        }
    }
    var answer = 1
    for (v in monkeys.map { it.numInspected }.sorted().reversed().take(2)) {
        answer *= v
    }
    return answer.toString()
}
