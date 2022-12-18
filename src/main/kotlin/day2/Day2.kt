package day2

import output.printAnswer

enum class Throw { ROCK, PAPER, SCISSORS }
enum class Result { LOSE, DRAW, WIN }

fun run(input: String, stage: String): String {
    part1(input, stage)
    part2(input, stage)
    return ""
}

data class Round1(val opp: Throw, val me: Throw)

fun String.toRound1(): Round1 {
    val parts = this.split(" ")
    val opp = when (parts[0]) {
        "A" -> Throw.ROCK
        "B" -> Throw.PAPER
        "C" -> Throw.SCISSORS
        else -> throw Exception("invalid throw")
    }
    val me = when (parts[1]) {
        "X" -> Throw.ROCK
        "Y" -> Throw.PAPER
        "Z" -> Throw.SCISSORS
        else -> throw Exception("invalid throw")
    }
    return Round1(opp, me)
}

fun roundResult(r: Round1): Result {
    return when {
        r.opp.ordinal == r.me.ordinal -> Result.DRAW
        r.opp.ordinal == (r.me.ordinal + 1) % 3 -> Result.LOSE
        else -> Result.WIN
    }
}

fun part1(input: String, stage: String) {
    val rounds = input.split("\n").map { it.toRound1() }
    var score = 0
    for (r in rounds) {
        when (roundResult(r)) {
            Result.WIN -> score += 6
            Result.DRAW -> score += 3
            else -> {}
        }
        when (r.me) {
            Throw.ROCK -> score += 1
            Throw.PAPER -> score += 2
            Throw.SCISSORS -> score += 3
        }
    }
    val want = if (stage == "problem") 15523 else 15
    printAnswer(score, want, "Part 1")
}

data class Round2(val opp: Throw, val res: Result)
fun String.toRound2(): Round2 {
    val parts = this.split(" ")
    val opp = when (parts[0]) {
        "A" -> Throw.ROCK
        "B" -> Throw.PAPER
        "C" -> Throw.SCISSORS
        else -> throw Exception("invalid throw")
    }
    val me = when (parts[1]) {
        "X" -> Result.LOSE
        "Y" -> Result.DRAW
        "Z" -> Result.WIN
        else -> throw Exception("invalid throw")
    }
    return Round2(opp, me)
}

fun Round2.winThrow(): Throw {
    return when (res) {
        Result.LOSE -> Throw.values()[(opp.ordinal - 1 + 3) % 3]
        Result.DRAW -> Throw.values()[opp.ordinal]
        Result.WIN -> Throw.values()[(opp.ordinal + 1) % 3]
    }
}
fun part2(input: String, stage: String) {
    val rounds = input.split("\n").map { it.toRound2() }
    var score = 0
    for (r in rounds) {
        val winScore = when (r.res) {
            Result.WIN -> 6
            Result.DRAW -> 3
            else -> 0
        }
        score += winScore
        val throwScore = when (r.winThrow()) {
            Throw.ROCK -> 1
            Throw.PAPER -> 2
            Throw.SCISSORS -> 3
        }
        score += throwScore
    }
    val want = if (stage == "problem") 15702 else 12
    printAnswer(score, want, "Part 2")
}
