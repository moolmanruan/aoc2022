package day23

import grid.Coord
import java.io.File

data class GifTextFile(
    val path: String,
    val delay: Int,
    val minX: Int,
    val maxX: Int,
    val minY: Int,
    val maxY: Int
)

fun GifTextFile.create() {
    val output = """
        delay:$delay
        scale:10
        ---
        b:00,00,00,ff
        w:ff,ff,ff,ff
        ---
    """.trimIndent()

    File(path).writeText(output)
}

fun GifTextFile.addFrame(elves: List<Elf>, delay: Int? = null) {
    val elfMap = elves.associateBy { it }
    val lines = mutableListOf<String>()
    for (y in maxY downTo minY) {
        val line = mutableListOf<String>()
        for (x in minX..maxX) {
            if (Coord(x, y) in elfMap) {
                line.add("w")
            } else {
                line.add("b")
            }
        }
        lines.add(line.joinToString(","))
    }
    File(path).appendText(
        """
-${if (delay != null) "d:$delay" else ""}
${lines.joinToString("\n")}"""
    )
}
