package day25

import output.printAnswer

fun run(input: String, stage: String) {
    part1(input, stage)
}

fun snafuToDecimal(input: String): Long {
    var total = 0L
    var base = 1L
    for (v in input.windowed(1).reversed()) {
        total += when (v) {
            "2" -> 2 * base
            "1" -> base
            "0" -> 0
            "-" -> -base
            "=" -> -2 * base
            else -> throw Exception("Invalid number")
        }
        base *= 5
    }
    return total
}

const val base = 5
const val halfBase = base / 2
fun decimalToSnafu(input: Long): String {
    var rem = input
    var res = ""
    while (rem != 0L) {
        val m = (rem + halfBase).mod(base)
        res = "=-012"[m] + res
        rem -= (m - halfBase)
        rem /= base
    }
    return res
}

fun part1(input: String, stage: String) {
    val sumAns = input.split("\n").map(::snafuToDecimal).sum()
    val want = if (stage == "problem") "2011-=2=-1020-1===-1" else "2=-1=0"
    printAnswer(decimalToSnafu(sumAns), want, "Part 1")
}
