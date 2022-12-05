data class Assignment(val start: Int, val end: Int)
data class Range(val a: Assignment, val b: Assignment)

fun Range.fullOverlap(): Boolean {
    return (a.start <= b.start && a.end >= b.end) || (b.start <= a.start && b.end >= a.end)
}

fun Range.partialOverlap(): Boolean {
    return when {
        a.start <= b.start -> a.end >= b.start
        else -> b.end >= a.start
    }
}

fun String.toRange(): Range {
    val vals = this.split(",")
    return Range(vals[0].toAssignment(), vals[1].toAssignment())
}

fun String.toAssignment(): Assignment {
    val vals = this.split("-")
    return Assignment(vals[0].toInt(), vals[1].toInt())
}
fun day4(input: String): String {
    val lines = input.split("\n")
    val partOne = lines.map(String::toRange).count { it.fullOverlap() }.toString()
    println("Part 1: $ANSI_BLUE$partOne$ANSI_RESET")
    return lines.map(String::toRange).count { it.partialOverlap() }.toString()
}
