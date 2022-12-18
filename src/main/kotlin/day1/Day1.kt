package day1

import output.printAnswer

fun run(input: String, stage: String): String {
    part1(input, stage)
    part2(input, stage)
    return ""
}

fun part1(input: String, stage: String) {
    val ans = input.split("\n\n").map {
        it.split("\n").map(String::toInt).sum()
    }.max()
    val want = if (stage == "problem") 71502 else 24000
    printAnswer(ans, want, "Part 1")
}

fun part2(input: String, stage: String) {
    val ans = input.split("\n\n").map {
        it.split("\n").map(String::toInt).sum()
    }.sortedDescending().take(3).sum()
    val want = if (stage == "problem") 208191 else 45000
    printAnswer(ans, want, "Part 2")
}
