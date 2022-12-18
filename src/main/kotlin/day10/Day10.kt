package day10
import grid.Coord
import output.printAnswer
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

fun run(input: String, stage: String) {
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
    val want1 = if (stage == "problem") 13720 else 13140
    printAnswer(signalStrength.sum(), want1, "Part 1")

    val screen = grid.NewGrid(40, 6, " ").toMutableGrid()
    for (i in cpuStates.indices) {
        cpu = cpuStates[i]
        val pixelPos = Coord(i % screen.width(), i / screen.width())
        if ((cpu.x - pixelPos.x).absoluteValue <= 1) {
            screen.setSafe(pixelPos.x, pixelPos.y, "█")
        }
    }
    val want2 = if (stage == "problem") PART2_PROBLEM else PART2_EXAMPLE
    printAnswer("\n" + screen, want2, "Part 2")
}

const val PART2_PROBLEM = """
████ ███  █  █ ███  █  █ ████  ██  █  █ 
█    █  █ █  █ █  █ █  █    █ █  █ █  █ 
███  ███  █  █ █  █ ████   █  █    ████ 
█    █  █ █  █ ███  █  █  █   █    █  █ 
█    █  █ █  █ █ █  █  █ █    █  █ █  █ 
█    ███   ██  █  █ █  █ ████  ██  █  █ """
const val PART2_EXAMPLE = """
██  ██  ██  ██  ██  ██  ██  ██  ██  ██  
███   ███   ███   ███   ███   ███   ███ 
████    ████    ████    ████    ████    
█████     █████     █████     █████     
██████      ██████      ██████      ████
███████       ███████       ███████     """
