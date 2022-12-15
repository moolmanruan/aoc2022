import grid.Coord
import grid.add
import grid.sub
import java.math.BigInteger
import kotlin.math.absoluteValue

enum class ObjectKind {
    SENSOR,
    BEACON
}

data class SensorInfo(val pos: Coord, val beacon: Coord)

fun SensorInfo.border(): List<Coord> {
    val dist = range()
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

fun day15(input: String, stage: String): String {
    val sensors = input.split("\n").map(String::toSensorInfo)
//    val coverages = sensors.map(::sensorCoverage)
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
    println("Part 1:$ANSI_BLUE $emptyCount$ANSI_WHITE want $want1$ANSI_RESET")

    val maxXY = if (stage == "example")20 else 4000000
    val distressBeacon = findBeacon(sensors, objMap, Coord(maxXY, maxXY))!!
    println("Distress beacon found at $distressBeacon")

    fun toFreq(c: Coord): BigInteger {
        val xBig = BigInteger.valueOf(c.x.toLong())
        val maxF = BigInteger.valueOf(4000000)
        val yBig = BigInteger.valueOf(c.y.toLong())
        return xBig.multiply(maxF) + yBig
    }
    val want = if (stage == "example") "56000011" else "10884459367718"
    return "${toFreq(distressBeacon)}$ANSI_WHITE want $want"
}

fun findBeacon(sensors: List<SensorInfo>, objMap: Map<Coord, ObjectKind>, maxPos: Coord): Coord? {
    for (border in sensors.map(SensorInfo::border)) {
        for (c in border) {
            for (dir in listOf(grid.Up, grid.Down, grid.Left, grid.Right)) {
                val p = c.add(dir)
                if (p.x < 0 ||
                    p.x > maxPos.x ||
                    p.y < 0 ||
                    p.y > maxPos.y ||
                    p in objMap ||
                    posUsed(sensors, p)
                ) {
                    continue
                }

                return p
            }
        }
    }
    return null
}

fun posUsed(sensors: List<SensorInfo>, pos: Coord): Boolean {
    for (s in sensors) {
        if (l1(s.pos, pos) <= s.range()) {
            return true
        }
    }
    return false
}

fun findBeaconSlightlyLessBruteForce(sensors: List<SensorInfo>, objMap: Map<Coord, ObjectKind>, maxPos: Coord): Coord? {
    var x = 0
    for (y in 0..maxPos.y) {
        println(y)
        x = 0
        while (x <= maxPos.x) {
            val c = Coord(x, y)
            if (c in objMap) {
                x++
                continue
            }
            var inRange = false
            for (s in sensors) {
                if (l1(s.pos, c) <= s.range()) {
                    x = s.mirrorXRight(c).x + 1
                    inRange = true
                    break
                }
            }
            if (!inRange) {
                return c
            }
        }
    }
    return null
}

fun findBeaconBruteForce(sensors: List<SensorInfo>, objMap: Map<Coord, ObjectKind>, maxPos: Coord): Coord? {
    for (x in 0..maxPos.x) {
        for (y in 0..maxPos.y) {
            val c = Coord(x, y)
            if (c in objMap) {
                continue
            }
            var inRange = false
            for (s in sensors) {
                if (l1(s.pos, c) <= s.range()) {
                    inRange = true
                    break
                }
            }
            if (!inRange) {
                return c
            }
        }
    }
    return null
}

fun l1(a: Coord, b: Coord): Int {
    val d = b.sub(a)
    return d.x.absoluteValue + d.y.absoluteValue
}
