package day6
import output.printAnswer

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

fun run(input: String, stage: String): String {
    val codes = input.split("\n")
    val ans1 = codes.map { process(it, 4) }
    val want1 = if (stage == "problem") "[1965]" else "[7, 5, 6, 10, 11]"
    printAnswer(ans1, want1, "Part 1")
    val ans2 = codes.map { process(it, 14) }
    val want2 = if (stage == "problem") "[2773]" else "[19, 23, 23, 29, 26]"
    printAnswer(ans2, want2, "Part 2")
    return ""
}
