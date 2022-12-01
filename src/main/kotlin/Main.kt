fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Too few arguments")
        return
    }
    val day = args[0].toIntOrNull()
    if (day == null) {
        println("Invalid day")
        return
    }

    run(day, args[1])
}

fun run(day: Int, stage: String) {
    try {
        val lines = readfile("day$day/$stage.txt")
        println("Day $day, Stage: $stage")
        val answer = when (day) {
            1 -> day1(lines)
            2 -> day2(lines)
            3 -> day3(lines)
            7 -> day7(lines)
            else -> ""
        }
        println("Answer: $answer")
    } catch (ex: Exception) {
        println(ex)
    }
}
