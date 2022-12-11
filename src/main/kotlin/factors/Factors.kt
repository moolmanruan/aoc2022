package factors

fun primeFactors(value: Int): List<Int> {
    var v = value
    val ff = mutableListOf<Int>()
    while (v > 1) {
        var n = 2
        while (v % n != 0) {
            n++
        }
        ff.add(n)
        v /= n
    }
    return ff.toList()
}

fun leastCommonMultiple(a: Int, b: Int): Int {
    if (a == b) {
        return a
    }
    val pfA = primeFactors(a)
    val pfB = primeFactors(b).toMutableList()

    val unusedPFA = mutableListOf<Int>()
    pfA.forEach { if (it in pfB) pfB.remove(it) else unusedPFA.add(it) }

    return unusedPFA.fold(1) { acc, v -> acc * v } * b
}
