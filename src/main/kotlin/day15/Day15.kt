package day15

import grid.Coord
import grid.minus
import output.printAnswer
import java.math.BigInteger
import kotlin.math.absoluteValue

enum class ObjectKind {
    SENSOR,
    BEACON
}

data class SensorInfo(val pos: Coord, val beacon: Coord)

fun SensorInfo.border(): List<Coord> {
    val dist = range() + 1
    val cc = mutableSetOf<Coord>()
    for (d in 0..dist) {
        val xd = dist - d
        val yd = dist - xd

        cc.add(Coord(pos.x - xd, pos.y - yd))
        cc.add(Coord(pos.x - xd, pos.y + yd))
        cc.add(Coord(pos.x + xd, pos.y - yd))
        cc.add(Coord(pos.x + xd, pos.y + yd))
    }
    return cc.toList()
}

fun SensorInfo.mirrorXRight(c: Coord): Coord {
    val diff = pos.x - c.x
    return Coord(pos.x + diff.absoluteValue, c.y)
}

fun SensorInfo.range(): Int {
    return l1(pos, beacon)
}

fun SensorInfo.inRange(c: Coord): Boolean {
    return l1(this.pos, c) <= range()
}

fun SensorInfo.reachMin(): Coord {
    return Coord(pos.x - range(), pos.y - range())
}
fun SensorInfo.reachMax(): Coord {
    val dist = l1(pos, beacon)
    return Coord(pos.x + dist, pos.y + dist)
}

fun String.toSensorInfo(): SensorInfo {
    val sp = this.split(":").first().trim().split(",")
    val bp = this.split(":").last().trim().split(",")
    val pos = Coord(sp.first().toInt(), sp.last().toInt())
    val beacon = Coord(bp.first().toInt(), bp.last().toInt())
    return SensorInfo(pos, beacon)
}

fun run(input: String, stage: String): String {
    val sensors = input.split("\n").map(String::toSensorInfo)

    val objMap = mutableMapOf<Coord, ObjectKind>()
    sensors.forEach {
        objMap[it.pos] = ObjectKind.SENSOR
        objMap[it.beacon] = ObjectKind.BEACON
    }

    var emptyCount = 0
    val minX = sensors.minOf { it.reachMin().x }
    val maxX = sensors.maxOf { it.reachMax().x }
    val y = if (stage == "example") 10 else 2000000
    for (x in minX..maxX) {
        val c = Coord(x, y)
        if (c in objMap) {
            continue
        }
        for (s in sensors) {
            if (l1(s.pos, c) <= s.range()) {
                emptyCount++
                break
            }
        }
    }
    val want1 = if (stage == "example") "26" else "5142231"
    printAnswer(emptyCount, want1, "Part 1")

    val maxXY = if (stage == "example")20 else 4000000
    val startTime = System.currentTimeMillis()
    val distressBeacon = findBeacon(sensors, objMap, Coord(maxXY, maxXY))!!
    val endTime = System.currentTimeMillis()
    println("Distress beacon found at $distressBeacon in ${(endTime - startTime) / 1000.0} seconds")

    fun toFreq(c: Coord): BigInteger {
        val xBig = BigInteger.valueOf(c.x.toLong())
        val maxF = BigInteger.valueOf(4000000)
        val yBig = BigInteger.valueOf(c.y.toLong())
        return xBig.multiply(maxF) + yBig
    }
    val want2 = if (stage == "example") "56000011" else "10884459367718"
    printAnswer(toFreq(distressBeacon), want2, "Part 2")
    return ""
}

fun findBeacon(sensors: List<SensorInfo>, objMap: Map<Coord, ObjectKind>, maxPos: Coord): Coord? {
    for (border in sensors.map(SensorInfo::border)) {
        val borderInBounds = border.filter {
            it.x >= 0 && it.x <= maxPos.x && it.y >= 0 && it.y <= maxPos.x
        }
        for (c in borderInBounds) {
            if (c !in objMap && !posUsed(sensors, c)) { return c }
        }
    }
    return null
}

fun posUsed(sensors: List<SensorInfo>, pos: Coord): Boolean {
    return sensors.find { it.inRange(pos) } != null
}

fun l1(a: Coord, b: Coord): Int {
    val d = b - a
    return d.x.absoluteValue + d.y.absoluteValue
}
