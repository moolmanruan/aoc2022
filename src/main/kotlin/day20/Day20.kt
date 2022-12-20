package day20

import output.printAnswer

fun run(input: String, stage: String) {
    part1(input, stage)
    part2(input, stage)
}

fun part1(input: String, stage: String) {
    val numbers = input.split("\n").map(String::toLong)
    val decoded = decode(numbers)
    val want = if (stage == "problem") 14526 else 3
    printAnswer(coords(decoded), want, "Part 1")
}

const val DECRYPTION_KEY = 811589153
fun part2(input: String, stage: String) {
    val numbers = input.split("\n").map(String::toLong)
        .map { it * DECRYPTION_KEY }
    val decoded = decode(numbers, 10)
    val want: Long = if (stage == "problem") 9738258246847 else 1623178306
    printAnswer(coords(decoded), want, "Part 2")
}

fun coords(ll: List<Long>): Long {
    val startIdx = ll.indexOf(0)
    val x = ll[(startIdx + 1000) % ll.size]
    val y = ll[(startIdx + 2000) % ll.size]
    val z = ll[(startIdx + 3000) % ll.size]
    println("coords $x $y $z")
    return x + y + z
}

fun decode(ll: List<Long>, times: Int = 1): List<Long> {
    val numsIndexed = ll.mapIndexed { i, v -> Pair(i, v) }.toMutableList()

    for (t in 1..times) {
        for (idx in ll.indices) {
            moveNumber(numsIndexed, idx)
        }
    }
    return numsIndexed.map { it.second }
}

fun moveNumber(ll: MutableList<Pair<Int, Long>>, idx: Int) {
    var curIdx = -1
    for (li in ll.indices) {
        if (ll[li].first == idx) {
            curIdx = li
            break
        }
    }
    assert(curIdx != -1)
    val moveValue = ll[curIdx].second
    val item = ll.removeAt(curIdx)
    val newIdx = (curIdx + moveValue).mod(ll.size)
    if (newIdx > 0) ll.add(newIdx, item) else ll.add(item)
}
