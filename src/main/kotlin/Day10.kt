import grid.Coord
import kotlin.math.absoluteValue

enum class CmdKind { NOOP, ADD }

data class Command(val kind: CmdKind, val number: Int?, val cycles: Int)

fun String.toCommand(): Command {
    val parts = this.trim().split(" ")
    return when (parts.first()) {
        "noop" -> Command(CmdKind.NOOP, null, 1)
        "addx" -> Command(CmdKind.ADD, parts.last().toInt(), 2)
        else -> throw Exception("unexpected command")
    }
}

data class CPU(var x: Int)

fun day10(input: String): String {
    val cmds = input.split("\n").map(String::toCommand)
    val cpuStates = mutableListOf(CPU(1))
    var cpu = cpuStates.last()
    for (cmd in cmds) {
        when (cmd.kind) {
            CmdKind.NOOP -> {
                cpuStates.add(CPU(cpu.x))
            }
            CmdKind.ADD -> {
                cpuStates.add(CPU(cpu.x))
                cpuStates.add(CPU(cpu.x + cmd.number!!))
            }
        }
        cpu = cpuStates.last()
    }

    val signalStrength = mutableListOf<Int>()
    for (i in 20.until(cpuStates.size) step 40) {
        signalStrength.add(cpuStates[i - 1].x * i)
    }
    println("part one: $ANSI_BLUE${signalStrength.sum()}$ANSI_RESET ${ANSI_WHITE}want 13720$ANSI_RESET")

    val screen = grid.NewGrid(40, 6, " ").toMutableGrid()
    for (i in cpuStates.indices) {
        cpu = cpuStates[i]
        val pixelPos = Coord(i % screen.width(), i / screen.width())
        if ((cpu.x - pixelPos.x).absoluteValue <= 1) {
            screen.setSafe(pixelPos.x, pixelPos.y, "█")
        }
    }
    return "${ANSI_WHITE}want FBURHZCH${ANSI_BLUE}\n$screen"
}
