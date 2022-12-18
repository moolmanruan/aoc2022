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
        if (ticksToOpen >= (maxTicks - entity.ticksUsed)) {
            if (attempt.entities.size > 1) {
                // Remove entity that's out of time
                val newEntities = attempt.entities.toMutableList()
                newEntities.removeAt(entityIdx)
                next.add(
                    state(
                        newEntities.toList(),
                        availableValves.map { it.copy() },
                        attempt.history.toMutableList().toList()
                    )
                )
            }
            continue
        }

        val hist = attempt.history.toMutableList()
        hist.add(Pair(entity.ticksUsed + ticksToOpen, valve.copy()))
        val newEntities = attempt.entities.toMutableList()
        newEntities[entityIdx] = entity(valve, entity.ticksUsed + ticksToOpen)
        next.add(
            state(
                newEntities.toList(),
                availableValves.map { it.copy() }.filter { it.name != valve.name }, // drop the one we just "opened"
                hist.toList()
            )
        )
    }
    return next
}

fun nextStates(attempt: state, maxTicks: Int): List<state> {
    val leastTicksUsed = attempt.entities.minOf { it.ticksUsed }
    for (i in attempt.entities.indices) {
        if (attempt.entities[i].ticksUsed == leastTicksUsed) {
            return nextValve(i, attempt, maxTicks)
        }
    }
    throw Exception("Must have an entity that matches the smallest value")
}

data class entity(val valve: Valve, val ticksUsed: Int)
data class state(val entities: List<entity>, val valves: List<Valve>, val history: List<Pair<Int, Valve>>)

fun state.flow(maxTicks: Int): Int {
    return history.map { (i, v) -> v.flowRate * (maxTicks - i) }.sum()
}

fun state.replay(maxTicks: Int): String {
    var totalFlow = 0
    var flowRate = 0
    val lines = mutableListOf<String>()
    for (i in 1..maxTicks) {
        totalFlow += flowRate
        for (v in history.filter { it.first == i }) {
            flowRate += v.second.flowRate
        }
        lines.add("$i: $totalFlow (+$flowRate)")
    }
    return lines.joinToString("\n")
}

fun run(input: String, stage: String) {
    val startTime = System.currentTimeMillis()
    val valves = toValves(input)

    val startingValve = valves.find { it.name == "AA" } ?: throw Exception("Can't find valve AA")
    val activeValves = valves.filter { it.flowRate != 0 }

    // Part 1
    // val maxTicks = 30
    // val startEntities = listOf(
    //    entity(startingValve, 0)
    // )
    // Part 2
    val maxTicks = 26
    val startEntities = listOf(
        entity(startingValve, 0),
        entity(startingValve, 0)
    )

    val start = state(
        startEntities,
        activeValves,
        emptyList()
    )

    val attempts = mutableListOf(start)
    var best: Int = Int.MIN_VALUE
    while (attempts.isNotEmpty()) {
        // Take the best on so far
        attempts.sortByDescending { it.flow(maxTicks) }

        val current = attempts.removeFirst()
        val next = nextStates(current, maxTicks)
        if (next.isEmpty()) {
            val answer = current.flow(maxTicks)
            if (answer > best) {
                println("> $answer (${System.currentTimeMillis() - startTime}ms)")
                best = answer
            }
        }
        attempts.addAll(next)
    }
    println("Time taken ${System.currentTimeMillis() - startTime}ms")

    // Part 1
    // return "$best want ${if (stage == "problem") "1460" else "1651"}"
    // Part 2
    println("$best want ${if (stage == "problem") "2117" else "1707"}")

    // problem alt 1724 part 1
}
