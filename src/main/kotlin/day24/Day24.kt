package day24

import output.printAnswer

fun run(input: String, stage: String) {
    part1(input, stage)
}

fun part1(input: String, stage: String) {
    val want = if (stage == "problem") null else null
    printAnswer(0, want, "Part 1")
}
