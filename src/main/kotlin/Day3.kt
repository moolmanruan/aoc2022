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
        println("${ANSI_YELLOW}Group too small$ANSI_RESET")
        return null
    }
    return g[0].find { g[1].contains(it) && g[2].contains(it) }
}

fun day3(input: String): String {
    val lines = input.split("\n")
    val sacks = lines.map(String::toRucksack)
    val partOne = sacks.map { it.duplicateItem().value() }.sum()
    println("Answer (part 1): $ANSI_BLUE$partOne$ANSI_RESET")

    val partTwo = lines.windowed(3, 3).map { groupItem(it).value() }.sum()
    return partTwo.toString()
}
