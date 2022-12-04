data class Assignment(val start: Int, val end: Int)
data class Pair(val a: Assignment, val b: Assignment)

fun Pair.fullOverlap(): Boolean {
    return (a.start <= b.start && a.end >= b.end) || (b.start <= a.start && b.end >= a.end)
}

fun Pair.partialOverlap(): Boolean {
    return when {
        a.start <= b.start -> a.end >= b.start
        else -> b.end >= a.start
    }
}

fun String.toPair(): Pair {
    val vals = this.split(",")
    return Pair(vals[0].toAssignment(), vals[1].toAssignment())
}

fun String.toAssignment(): Assignment {
    val vals = this.split("-")
    return Assignment(vals[0].toInt(), vals[1].toInt())
}
fun day4(lines: List<String>): String {
    val partOne = lines.map(String::toPair).count { it.fullOverlap() }.toString()
    println("Part 1: $ANSI_BLUE$partOne$ANSI_RESET")
    return lines.map(String::toPair).count { it.partialOverlap() }.toString()
}
