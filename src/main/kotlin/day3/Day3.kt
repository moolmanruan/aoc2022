package day3

import output.printAnswer

typealias Item = Char?

fun Item.value(): Int {
    return when {
        this == null -> 0
        this.isLowerCase() -> this.code - 96
        else -> this.code - 38
    }
}
data class Rucksack(val left: String, val right: String)

fun Rucksack.duplicateItem(): Item {
    return this.left.find { this.right.contains(it) }
}

fun String.toRucksack(): Rucksack {
    return Rucksack(
        this.subSequence(0, this.length / 2).toString(),
        this.subSequence(this.length / 2, this.length).toString()
    )
}

fun groupItem(g: List<String>): Item {
    if (g.size < 3) {
        println("${output.ANSI_YELLOW}Group too small${output.ANSI_RESET}")
        return null
    }
    return g[0].find { g[1].contains(it) && g[2].contains(it) }
}

fun run(input: String, stage: String): String {
    val lines = input.split("\n")
    val sacks = lines.map(String::toRucksack)
    val part1 = sacks.map { it.duplicateItem().value() }.sum()
    val want1 = if (stage == "problem") 8153 else 157
    printAnswer(part1, want1, "Part 1")

    val part2 = lines.windowed(3, 3).map { groupItem(it).value() }.sum()
    val want2 = if (stage == "problem") 2342 else 70
    printAnswer(part2, want2, "Part 1")
    return ""
}
