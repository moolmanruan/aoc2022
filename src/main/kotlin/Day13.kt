data class CodePair(val left: String, val right: String)

fun String.toPair(): CodePair {
    val parts = this.split("\n")
    return CodePair(parts.first(), parts.last())
}

fun parseValue(value:String):List<String> {
    if (!value.startsWith("[")) {
        return listOf(value)
    }
    val v = value.substring(1.until(value.length-1))

    var nested =0
    var part = ""
    val result = mutableListOf<String>()
    v.windowed(1).forEach {
        when (it) {
            "["->nested++
            "]"->nested--
        }
        when (it) {
            ","->if (nested==0) {
                result.add(part)
                part = ""
            } else part+=it
            else->part+=it
        }
    }
    if (part.isNotEmpty()) {
        result.add(part)
    }
    return result.toList()
}

fun comparePair(a:String, b:String): Int{
    val aVal = a.toIntOrNull()
    val bVal = b.toIntOrNull()
    if (aVal != null && bVal!=null) {
        return when {
            aVal == bVal->0
            aVal<bVal->-1
            else ->1
        }
    }

    val aParts = parseValue(a)
    val bParts = parseValue(b)

    if (aParts.isEmpty() && bParts.isNotEmpty()) {
        return -1
    } else if (aParts.isNotEmpty() && bParts.isEmpty()) {
        return 1
    }

    aParts.forEachIndexed { i, part ->
        if (i in bParts.indices) {
            val cVal = comparePair(part, bParts[i])
            if (cVal != 0) {
                return cVal
            }
        }
    }
    if (aParts.size < bParts.size) {
        return -1
    } else if (aParts.size > bParts.size) {
        return 1
    }
    return 0
}

fun checkOrder(pairs: List<CodePair>):List<Int> {
    return pairs
        .mapIndexed { i, p ->
            val cVal = comparePair(p.left, p.right)
            if (cVal <= 0) {
                i
            } else {
                -1
            }
        }
        .filter { it>=0 }
}

fun day13(input: String): String {
    val pairs = input.split("\n\n").map(String::toPair)

    val correctlyOrderedPairs = checkOrder(pairs)
    // sum of indices in the right order
    return correctlyOrderedPairs.sumOf{it+1}.toString()
}
