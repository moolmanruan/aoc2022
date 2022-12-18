package day18

import output.printAnswer

data class Vec3(val x: Int, val y: Int, val z: Int)

operator fun Vec3.plus(other: Vec3): Vec3 {
    return Vec3(x + other.x, y + other.y, z + other.z)
}

fun String.toVec3(): Vec3 {
    val p = this.split(",")
    return Vec3(p[0].toInt(), p[1].toInt(), p[2].toInt())
}

fun run(input: String, stage: String): String {
    part1(input, stage)
    return ""
}

fun part1(input: String, stage: String) {
    val drops = input.split("\n").map(String::toVec3).toSet()

    val ans = drops.fold(0) { count, drop ->
        count + listOf(
            Vec3(1, 0, 0),
            Vec3(-1, 0, 0),
            Vec3(0, 1, 0),
            Vec3(0, -1, 0),
            Vec3(0, 0, 1),
            Vec3(0, 0, -1)
        ).filter {
            drop + it !in drops
        }.count()
    }
    printAnswer(ans, 64)
}
