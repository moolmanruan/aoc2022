package output

fun <T> printAnswer(got: T, want: T? = null, prefix: String = "") {
    var out = "$ANSI_BLUE$got$ANSI_RESET"
    if (want != null && want.toString().isNotEmpty()) {
        val colour = if (got.toString() == want.toString()) ANSI_WHITE else ANSI_RED
        out += "$ANSI_WHITE want $colour$want$ANSI_RESET"
    }
    if (prefix.isNotEmpty()) {
        out = "$ANSI_PURPLE$prefix$ANSI_RESET: $out"
    }
    println(out)
}

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"
