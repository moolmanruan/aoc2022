data class CodePair(val left: String, val right: String)

fun String.toPair(): CodePair {
    val parts = this.split("\n")
    return CodePair(parts.first(), parts.last())
}

fun parseStringList(value: String): List<String> {
    if (!(value.startsWith("[") && value.endsWith("]"))) {
        return listOf(value)
    }
    val v = value.drop(1).dropLast(1)

    var nested = 0
    var part = ""
    val result = mutableListOf<String>()
    v.windowed(1).forEach {
        when (it) {
            "[" -> nested++
            "]" -> nested--
        }
        if (it == "," && nested == 0) {
            result.add(part)
            part = ""
        } else {
            part += it
        }
    }
    if (part.isNotEmpty()) result.add(part)
    return result.toList()
}

fun compareInt(a: Int, b: Int): Int {
    return when {
        a < b -> -1
        a > b -> 1
        else -> 0
    }
}

fun comparePair(a: String, b: String): Int {
    val aVal = a.toIntOrNull()
    val bVal = b.toIntOrNull()
    if (aVal != null && bVal != null) {
        return compareInt(aVal, bVal)
    }

    val aParts = parseStringList(a)
    val bParts = parseStringList(b)

    aParts.forEachIndexed { i, part ->
        if (i in bParts.indices) {
            val cVal = comparePair(part, bParts[i])
            if (cVal != 0) {
                return cVal
            }
        }
    }

    // Lists are equal so far, check sizes...
    return when {
        aParts.size < bParts.size -> -1
        aParts.size > bParts.size -> 1
        else -> 0
    }
}

fun checkOrder(pairs: List<CodePair>): List<Int> {
    return pairs
        .mapIndexed { i, p -> if (comparePair(p.left, p.right) <= 0) i else -1 }
        .filter { it >= 0 }
}

fun day13(input: String): String {
    val pairs = input.split("\n\n").map(String::toPair)

    val correctlyOrderedPairs = checkOrder(pairs)
    // sum of indices in the right order
    val part1 = correctlyOrderedPairs.sumOf { it + 1 }.toString()
    println("Part 1: $part1 $ANSI_WHITE(want 5625, example 13)")

    val codes = input.replace("\n\n", "\n").split("\n").toMutableList()
    codes.add("[[2]]")
    codes.add("[[6]]")
    val sortedCodes = codes.sortedWith(::comparePair)

    val a = sortedCodes.indexOf("[[2]]") + 1
    val b = sortedCodes.indexOf("[[6]]") + 1

    return "${a * b} $ANSI_WHITE(want 23111, example 140)"
}
