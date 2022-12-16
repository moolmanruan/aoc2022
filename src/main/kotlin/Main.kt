
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

fun main(args: Array<String>) {
    if (args.size < 3) {
        printErr("Too few arguments")
        return
    }
    val year = args[0].toIntOrNull()
    if (year == null) {
        printErr("Invalid year")
        return
    }

    val day = args[1].toIntOrNull()
    if (day == null) {
        printErr("Invalid day")
        return
    }

    run(year, day, args[2])
}

fun run(year: Int, day: Int, stage: String) {
    try {
        val inputFile = File("/Users/ruan/aoc2022/src/main/resources/$year/$day/$stage.txt")

        if (!inputFile.exists()) {
            if (stage == "problem") {
                downloadFile(2022, day, inputFile)
            } else {
                Path(inputFile.parent).createDirectories()
                inputFile.createNewFile()
            }
        }

        val input = inputFile.readText().trim()

        println("Day: ${ANSI_GREEN}$day$ANSI_RESET, Stage: ${ANSI_GREEN}$stage$ANSI_RESET")
        val answer = when (day) {
            1 -> day1(input)
            2 -> day2(input)
            3 -> day3(input)
            4 -> day4(input)
            5 -> day5(input)
            6 -> day6(input)
            7 -> day7(input)
            8 -> day8(input)
            9 -> day9(input)
            10 -> day10(input)
            11 -> day11(input)
            12 -> day12(input)
            13 -> day13(input)
            14 -> day14(input)
            15 -> day15(input, stage)
            151 -> day15.run(input)
            16 -> day16.run(input, stage)
            17 -> day17.run(input, stage)
            else -> ""
        }
        println("${ANSI_RESET}Answer: $ANSI_BLUE$answer$ANSI_RESET")
    } catch (ex: Exception) {
        printErr(ex.toString())
    }
}

fun downloadFile(year: Int, day: Int, path: File) {
    Path(path.parent).createDirectories()
    print(ANSI_PURPLE)
    println("Downloading input file for year $year day $day to $path")
    print(ANSI_RESET)
    val p = Runtime.getRuntime().exec("aocdl -year $year -day $day -output $path".split(" ").toTypedArray())
    p.waitFor(10, TimeUnit.SECONDS)
}

fun printErr(text: String) {
    println("$ANSI_RED$text$ANSI_RESET")
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
