package day16

import java.lang.Exception

data class Valve(
    val name: String,
    val flowRate: Int,
    // The cost to move from this valve to another
    val moveCost: List<Int?>,
    var pos: Int
)

fun valveName(input: String): String {
    val results = Regex("""Valve ([A-Z]+)""").find(input) ?: throw Exception("Failed to parse name")
    val (_, name) = results.groupValues
    return name
}

fun valveRate(input: String): Int {
    val results = Regex("""rate=(\d+)""").find(input) ?: throw Exception("Failed to parse rate")
    val (_, rate) = results.groupValues
    return rate.toInt()
}

fun valveTunnels(input: String, nameToPos: Map<String, Int>): List<Int> {
    val results = Regex("""valves? (.*)""").find(input) ?: throw Exception("Failed to parse tunnerl")
    val (_, tunnels) = results.groupValues
    return tunnels.replace(" ", "").split(",")
        .map { nameToPos[it] }.filterNotNull()
}

data class Path(val curPos: Int, val steps: List<Int>)
fun moveCostTo(from: Int, to: Int, tunnels: List<List<Int>>): Int? {
    val paths = mutableListOf(Path(from, emptyList()))
    val reached = mutableMapOf<Int, Int>()
    while (paths.isNotEmpty()) {
        val path = paths.removeFirst()

        if (path.curPos == to) {
            return path.steps.size
        }

        tunnels[path.curPos].forEach {
            if (it !in reached) {
                val p = Path(it, path.steps.toMutableList() + path.curPos)
                reached[it] = p.steps.size
                paths.add(p)
            }
        }
    }
    return null
}

fun toValves(input: String): List<Valve> {
    val valveLines = input.split("\n")
    val nameToPos = mutableMapOf<String, Int>()
    valveLines.forEachIndexed { i, v -> nameToPos[valveName(v)] = i }

    val tunnels = valveLines.map { valveTunnels(it, nameToPos) }

    val valves = mutableListOf<Valve>()
    val indices = valveLines.indices
    for (i in indices) {
        val l = valveLines[i]
        valves.add(
            Valve(
                valveName(l),
                valveRate(l),
                indices.map { moveCostTo(i, it, tunnels) ?: throw Exception("Failed to find a best path") },
                i
            )
        )
    }

    return valves
}

fun nextValve(entityIdx: Int, attempt: state, maxTicks: Int): List<state> {
    val entity = attempt.entities[entityIdx]
    val next = mutableListOf<state>()
    val availableValves = attempt.valves.filter { it.flowRate != 0 }
    for (valve in availableValves) {
        // ignore valves we can reach in time
        val ticksToOpen = entity.valve.moveCost[valve.pos]!! + 1
        if (ticksToOpen >= (maxTicks - entity.ticksUsed)) { continue }

        val hist = attempt.history.toMutableMap()
        hist[entity.ticksUsed + ticksToOpen] = valve.copy()
        val newEntities = attempt.entities.toMutableList()
        newEntities[entityIdx] = entity(valve, entity.ticksUsed + ticksToOpen)
        next.add(
            state(
                newEntities.toList(),
                availableValves.map { it.copy() }.filter { it.name != valve.name }, // drop the one we just "opened"
                hist.toMap()
            )
        )
    }
    return next
}

fun nextStates(attempt: state, maxTicks: Int): List<state> {
    return nextValve(0, attempt, maxTicks)

//    return if (attempt.elephant == null) {
//    } else {
//        if (attempt.me.ticksUsed <= attempt.elephant.ticksUsed) {
//            nextValve(attempt.me, attempt, maxTicks)
//        } else {
//            nextValve(attempt.elephant, attempt, maxTicks)
//        }
//    }
}

data class entity(val valve: Valve, val ticksUsed: Int)
data class state(val entities: List<entity>, val valves: List<Valve>, val history: Map<Int, Valve>)

fun state.flow(maxTicks: Int): Int {
    return history.map { (i, v) -> v.flowRate * (maxTicks - i) }.sum()
}

fun state.replay(maxTicks: Int): String {
    var totalFlow = 0
    var flowRate = 0
    val lines = mutableListOf<String>()
    for (i in 1..maxTicks) {
        totalFlow += flowRate
        if (i in history) {
            flowRate += history[i]?.flowRate ?: 0
        }
        lines.add("$i: $totalFlow (+$flowRate)")
    }
    return lines.joinToString("\n")
}

fun run(input: String, stage: String): String {
    val valves = toValves(input)

    val maxTicks = 30
    val startingValve = valves.find { it.name == "AA" } ?: throw Exception("Can't find valve AA")
    val activeValves = valves.filter { it.flowRate != 0 }
    val start = state(
        listOf(entity(startingValve, 0)),
        activeValves,
        emptyMap()
    )

    val attempts = mutableListOf(start)
    val completeAttempts = mutableListOf<state>()
    while (attempts.isNotEmpty()) {
        // Take the best on so far
        attempts.sortByDescending { it.flow(maxTicks) }

        val current = attempts.removeFirst()
        val next = nextStates(current, maxTicks)
        if (next.isEmpty()) {
            completeAttempts.add(current)
        }
        attempts.addAll(next)
    }

    val best = completeAttempts.sortedByDescending { it.flow(maxTicks) }.first()

    return "${best.flow(maxTicks)} want ${if (stage == "problem") "1460" else "1651"}"
}
