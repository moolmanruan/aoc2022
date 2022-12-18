package day18

import output.printAnswer

data class Vec3(val x: Int, val y: Int, val z: Int)

operator fun Vec3.plus(other: Vec3): Vec3 {
    return Vec3(x + other.x, y + other.y, z + other.z)
}

val directions = listOf(
    Vec3(1, 0, 0),
    Vec3(-1, 0, 0),
    Vec3(0, 1, 0),
    Vec3(0, -1, 0),
    Vec3(0, 0, 1),
    Vec3(0, 0, -1)
)

fun String.toVec3(): Vec3 {
    val p = this.split(",")
    return Vec3(p[0].toInt(), p[1].toInt(), p[2].toInt())
}

fun run(input: String, stage: String): String {
    part1(input, stage)
    part2(input, stage)
    return ""
}

fun part1(input: String, stage: String) {
    val drops = input.split("\n").map(String::toVec3).toSet()
    val ans = surfaceSize(drops)
    val want = if (stage == "problem") 3550 else 64
    printAnswer(ans, want, "Part 1")
}

fun part2(input: String, stage: String) {
    val drops = input.split("\n").map(String::toVec3).toSet()
    val ans = surfaceSize(fillCavities(drops))
    val want = if (stage == "problem") 2028 else 58
    printAnswer(ans, want, "Part 2")
}

fun surfaceSize(drops: Set<Vec3>): Int {
    return drops.fold(0) { count, drop ->
        count + directions.count { drop + it !in drops }
    }
}

fun fillCavities(drops: Set<Vec3>): Set<Vec3> {
    val filled = drops.toMutableSet()
    val maxCavitySize = drops.size
    drops.forEach { drop ->
        directions.forEach { dir ->
            filled.addAll(
                cavity(drop + dir, filled, maxCavitySize)
            )
        }
    }
    return filled
}

// cavity returns the set of points that is fully bounded by the input points
fun cavity(init: Vec3, drops: Set<Vec3>, max: Int): Set<Vec3> {
    val toCheck = mutableListOf(init)
    val cavityDrops = mutableSetOf(init)
    while (toCheck.isNotEmpty()) {
        val drop = toCheck.removeFirst()
        directions.forEach {
            val d = drop + it
            if (d !in drops && d !in cavityDrops) {
                toCheck.add(d)
                cavityDrops.add(d)
            }
        }
        // If we're above the max we assume we're not in a cavity
        if (cavityDrops.size > max) {
            return emptySet()
        }
    }
    return cavityDrops
}
