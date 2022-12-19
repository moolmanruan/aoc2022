package day19

import output.printAnswer

data class Resources(
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geode: Int
)

operator fun Resources.plus(other: Resources): Resources {
    return Resources(
        ore + other.ore,
        clay + other.clay,
        obsidian + other.obsidian,
        geode + other.geode
    )
}

operator fun Resources.minus(other: Resources): Resources {
    return Resources(
        ore - other.ore,
        clay - other.clay,
        obsidian - other.obsidian,
        geode - other.geode
    )
}

operator fun Resources.times(by: Int): Resources {
    return Resources(
        ore * by,
        clay * by,
        obsidian * by,
        geode * by
    )
}

data class RobotCosts(
    val ore: Resources,
    val clay: Resources,
    val obsidian: Resources,
    val geode: Resources
)

data class Blueprint(val id: Int, val robotCosts: RobotCosts)

data class Robots(
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geode: Int
)

operator fun Robots.plus(other: Robots): Robots {
    return Robots(
        ore + other.ore,
        clay + other.clay,
        obsidian + other.obsidian,
        geode + other.geode
    )
}

fun Resources.betterThan(other: Resources): Boolean {
    return this != other &&
        geode >= other.geode &&
        obsidian >= other.obsidian &&
        clay >= other.clay &&
        ore >= other.ore
}

operator fun Resources.compareTo(other: Resources): Int {
    return when {
        geode != other.geode -> if (geode > other.geode) 1 else -1
        obsidian != other.obsidian -> if (obsidian > other.obsidian) 1 else -1
        clay != other.clay -> if (clay > other.clay) 1 else -1
        ore != other.ore -> if (ore > other.ore) 1 else -1
        else -> 0
    }
}

operator fun Robots.compareTo(other: Robots): Int {
    return when {
        geode != other.geode -> if (geode > other.geode) 1 else -1
        obsidian != other.obsidian -> if (obsidian > other.obsidian) 1 else -1
        clay != other.clay -> if (clay > other.clay) 1 else -1
        ore != other.ore -> if (ore > other.ore) 1 else -1
        else -> 0
    }
}

fun Robots.isEmpty(): Boolean {
    return ore == 0 && clay == 0 && obsidian == 0 && geode == 0
}
fun Robots.isNotEmpty(): Boolean {
    return !isEmpty()
}

val lineRegex = """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()

fun String.toBlueprint(): Blueprint {
    val values = lineRegex.find(this)!!.groupValues.drop(1).map { it.toInt() }
    val id = values[0]
    val oreOre = values[1]
    val clayOre = values[2]
    val obsOre = values[3]
    val obsClay = values[4]
    val geoOre = values[5]
    val geoObs = values[6]
    return Blueprint(
        id,
        RobotCosts(
            Resources(oreOre, 0, 0, 0),
            Resources(clayOre, 0, 0, 0),
            Resources(obsOre, obsClay, 0, 0),
            Resources(geoOre, 0, geoObs, 0)
        )
    )
}

fun run(input: String, stage: String) {
    var time = System.currentTimeMillis()
    part1(input, stage)
    println("Time taken ${System.currentTimeMillis() - time}ms")
    time = System.currentTimeMillis()
    part2(input, stage)
    println("Time taken ${System.currentTimeMillis() - time}ms")
}

const val TICKS = 24
fun part1(input: String, stage: String) {
    val blueprints = input.split("\n").map(String::toBlueprint)
    val ans = blueprints.map {
        maxGeodes(it, TICKS) * it.id // blueprint quality
    }.sum()
    val want = if (stage == "problem") 1023 else 33
    printAnswer(ans, want, "Part 1")
}

const val TICKS_PART2 = 32
fun part2(input: String, stage: String) {
    val blueprints = input.split("\n").map(String::toBlueprint).take(3)
    val ans = blueprints.map {
        val m = maxGeodes(it, TICKS_PART2)
        m
    }.fold(1) { acc, v -> acc * v }
    val want = if (stage == "problem") 26 * 52 * 10 else 56 * 62
    printAnswer(ans, want, "Part 2")
}

data class State(
    val tick: Int,
    val robots: Robots,
    val resources: Resources
)

fun maxGeodes(b: Blueprint, maxTicks: Int): Int {
    val initialState = State(
        1,
        Robots(1, 0, 0, 0),
        Resources(0, 0, 0, 0)
    )

    var states = mutableListOf(initialState)
    for (tick in 1..maxTicks) {
        val tickStates = filterStates(states)
        states.clear()

        assert(tickStates.isNotEmpty())
        val next = mutableSetOf<State>()

        for (state in tickStates) {
            next.addAll(nextStates(state, b.robotCosts))
        }

        states = next.toMutableList()
    }

    val maxGeodes = states.maxOf { it.resources.geode }

    return maxGeodes
}

fun filterStates(states: MutableList<State>): List<State> {
    states.sortWith { sa, sb ->
        if (sa.resources.geode != sb.resources.geode) {
            if (sa.resources.geode > sb.resources.geode) 1 else -1
        } else {
            val rc = sa.robots.compareTo(sb.robots)
            if (rc == 0) sa.resources.compareTo(sb.resources) else rc
        }
    }
    states.reverse()

    val tickStates = mutableListOf<State>()
    // Only keep state that are objectively better than others
    states.groupBy { it.robots }.forEach { (r, rs) ->
        rs.forEach { state ->
            if (!rs.any { it.resources.betterThan(state.resources) }) {
                tickStates.add(state)
            }
        }
    }

    return tickStates.take(5000)
}

fun nextStates(state: State, robotCosts: RobotCosts): Set<State> {
    // robots mine for ore
    val newResources = Resources(
        state.resources.ore + state.robots.ore,
        state.resources.clay + state.robots.clay,
        state.resources.obsidian + state.robots.obsidian,
        state.resources.geode + state.robots.geode
    )

    // check for possible next builds
    val builds = possibleBuild(robotCosts, state.resources)
    val newStates = mutableSetOf<State>()
    builds.forEach { build ->
        val leftResources = newResources.copy() -
            robotCosts.ore * build.ore -
            robotCosts.clay * build.clay -
            robotCosts.obsidian * build.obsidian -
            robotCosts.geode * build.geode
        newStates.add(State(state.tick + 1, state.robots + build, leftResources))
    }
    return newStates
}

fun possibleBuild(costs: RobotCosts, resources: Resources): List<Robots> {
    return listOf(
        Robots(0, 0, 0, 1),
        Robots(0, 0, 1, 0),
        Robots(0, 1, 0, 0),
        Robots(1, 0, 0, 0),
        Robots(0, 0, 0, 0)
    ).filter { canBuild(it, costs, resources) }
}

fun canBuild(robots: Robots, costs: RobotCosts, resources: Resources): Boolean {
    val totalCost = costs.ore * robots.ore +
        costs.clay * robots.clay +
        costs.obsidian * robots.obsidian +
        costs.geode * robots.geode
    return resources.ore >= totalCost.ore &&
        resources.clay >= totalCost.clay &&
        resources.obsidian >= totalCost.obsidian &&
        resources.geode >= totalCost.geode
}
