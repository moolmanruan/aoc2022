import grid.Coord
import grid.sub
import kotlin.math.absoluteValue

enum class ObjectKind {
    EMPTY,
    SENSOR,
    BEACON
}

data class SensorInfo(val pos: Coord, val beacon: Coord)

fun SensorInfo.range(): Int {
    return l1(pos, beacon)
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

fun day15(input: String): String {
    val sensors = input.split("\n").map(String::toSensorInfo)
//    val coverages = sensors.map(::sensorCoverage)
    val objMap = mutableMapOf<Coord, ObjectKind>()
    sensors.forEach {
        objMap[it.pos] = ObjectKind.SENSOR
        objMap[it.beacon] = ObjectKind.BEACON
    }
//    for (c in coverages) {
//        c.forEach {
//            if (it !in objMap) {
//                objMap[it] = ObjectKind.EMPTY
//            }
//        }
//    }

    var emptyCount = 0
    val minX = sensors.minOf { it.reachMin().x }
    val maxX = sensors.maxOf { it.reachMax().x }
//    val y = 10
    val y = 2000000
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
//    objMap.forEach { k, v ->
//        if (k.y == checkY && v == ObjectKind.EMPTY) {
//            emptyCount++
//        }
//    }
    return emptyCount.toString()
}

fun sensorCoverage(sensor: SensorInfo): List<Coord> {
    val dist = l1(sensor.pos, sensor.beacon)
    val m = mutableListOf<Coord>()
    for (x in sensor.pos.x - dist..sensor.pos.x + dist) {
        for (y in sensor.pos.y - dist..sensor.pos.y + dist) {
            val pos = Coord(x, y)
            if (l1(sensor.pos, pos) <= dist) {
                m.add(pos)
            }
        }
    }
    return m
}
fun sensorCoverage(sensor: Coord, beacons: List<Coord>): List<Coord> {
    val beaconsClosest = beacons.sortedBy { l1(sensor, it) }
    val dist = l1(sensor, beaconsClosest.first())
    val m = mutableListOf<Coord>()
    for (x in sensor.x - dist..sensor.x + dist) {
        for (y in sensor.y - dist..sensor.y + dist) {
            val pos = Coord(x, y)
            if (l1(sensor, pos) <= dist) {
                m.add(pos)
            }
        }
    }
    return m
}

fun l1(a: Coord, b: Coord): Int {
    val d = b.sub(a)
    return d.x.absoluteValue + d.y.absoluteValue
}
