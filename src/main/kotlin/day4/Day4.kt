package day4

import output.printAnswer

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
    val parts = this.split(",")
    return Range(parts[0].toAssignment(), parts[1].toAssignment())
}

fun String.toAssignment(): Assignment {
    val parts = this.split("-")
    return Assignment(parts[0].toInt(), parts[1].toInt())
}
fun run(input: String, stage: String) {
    val ranges = input.split("\n").map(String::toRange)
    val partOne = ranges.count { it.fullOverlap() }.toString()
    val want1 = if (stage == "problem") 433 else 2
    printAnswer(partOne, want1, "Part 1")

    val part2 = ranges.count { it.partialOverlap() }.toString()
    val want2 = if (stage == "problem") 852 else 4
    printAnswer(part2, want2, "Part 2")
}
