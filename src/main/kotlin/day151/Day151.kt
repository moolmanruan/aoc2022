package day151

import grid.Coord
import grid.l1Distance
import kotlin.math.max
import kotlin.math.min

data class AreaVec2(val i: Int, val j: Int)

fun AreaVec2.toCoord(): Coord {
    return Coord((i + j) / 2, (-i + j) / 2)
}

fun AreaVec2.x(): Double {
    return (i + j) / 2.0
}
fun AreaVec2.y(): Double {
    return (-i + j) / 2.0
}

operator fun AreaVec2.plus(other: AreaVec2): AreaVec2 {
    return AreaVec2(i + other.i, j + other.j)
}
operator fun AreaVec2.minus(other: AreaVec2): AreaVec2 {
    return AreaVec2(i - other.i, j - other.j)
}

fun Coord.toAreaVec2(): AreaVec2 {
    return AreaVec2(x - y, x + y)
}

// Area is rectangular area rotation 45º on a cartesian map.
// `pos` is the left corner of the area
// `size` is the number of positions in each direction
//   `i` indicating the number in the +x,-y direction
//   `j` indicating the number in the +x,+y direction
data class Area(val pos: AreaVec2, val size: AreaVec2)

fun Area.overlap(other: Area): Area? {
    if (this == other) {
        return this
    }
    val a = pos
    val b = other.pos
//    println("a $pos b ${other.pos}")

    val iMin = max(a.i, b.i)
    val iMax = min(a.i + size.i, b.i + other.size.i)
//    println("i $iMin $iMax")
    if (iMin >= iMax) {
        return null
    }
    val jMin = max(a.j, b.j)
    val jMax = min(a.j + size.j, b.j + other.size.j)
//    println("j $jMin $jMax")
    if (jMin >= jMax) {
        return null
    }
    return Area(
        AreaVec2(iMin, jMin),
        AreaVec2(iMax - iMin, jMax - jMin)
    )
}

fun Area.points(): Long {
    return if ((pos.i + pos.j) % 2 == 0) {
        ((size.i.toLong() + 1) * (size.j.toLong() + 1) + 1) / 2
    } else {
        (size.i.toLong() + 1) * (size.j.toLong() + 1) / 2
    }
}
fun Area.pointsOnAndAboveX(x1: Int): Long {
    val cxMin = pos.x()
    if (x1 < cxMin) {
        return points()
    }

    val cxMax = (pos + size).x()
    if (x1 > cxMax) {
        return 0
    }
    fun pyramid(b: Int): Int {
        return b * (b + 1)
    }

    val x = (x1 - cxMin).toInt()
    val xBetween = (cxMin * 2).toInt() % 2 == 1
    val minSize = min(size.i, size.j)
    val maxSize = max(size.i, size.j)

    var sub = pyramid(x)
    var xOverMin = x - minSize / 2

    if (!xBetween) {
        sub -= x
        xOverMin -= 1
    }

    if (xOverMin > 0) {
        sub -= pyramid(xOverMin)
    }
    val xOverMax = x - 1 - maxSize / 2
    if (xOverMax > 0) {
        sub -= pyramid(xOverMax)
    }
    return points() - sub
}

fun String.toArea(): Area {
    val sp = this.split(":").first().trim().split(",")
    val bp = this.split(":").last().trim().split(",")
    val pos = Coord(sp.first().toInt(), sp.last().toInt())
    val beacon = Coord(bp.first().toInt(), bp.last().toInt())
    val d = pos.l1Distance(beacon)
    return Area(Coord(pos.x - d, pos.y).toAreaVec2(), AreaVec2(d * 2, d * 2))
}

fun run(input: String, stage: String): String {
    val areas = input.split("\n").map(String::toArea)

    val overlaps = mutableListOf<Area>()
    for (ia in 0 until areas.size - 1) {
        for (ib in ia + 1 until areas.size) {
            val overlap = areas[ia].overlap(areas[ib])
            if (overlap != null) {
            }
        }
    }
    return ""
}
