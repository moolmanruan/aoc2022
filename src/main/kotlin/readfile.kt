import java.io.File

fun readfile(filename: String): List<String> {
    return File("/Users/ruan/aoc2022/src/main/resources/$filename").readLines()
}