
fun different(code: String): Boolean {
    for (ci in 0 until code.length - 1) {
        if (code[ci] in code.substring(ci + 1, code.length)) {
            return false
        }
    }
    return true
}

fun process(code: String, size: Int): Int {
    val index = code
        .windowed(size, 1)
        .map { different(it) }
        .indexOf(true)
    if (index < 0) {
        return index
    }
    return index + size
}

fun day6(input: String): String {
    val codes = input.split("\n")
    println("Part one: $ANSI_BLUE${codes.map { process(it, 4) }.joinToString("\n")}$ANSI_RESET")
    return codes.map { process(it, 14) }.joinToString("\n")
}
