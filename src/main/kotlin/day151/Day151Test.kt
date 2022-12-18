package day151

import grid.Coord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestAreaVec2 {
    @Test
    fun toCoord() {
        assertEquals(Coord(1, 0), AreaVec2(1, 1).toCoord())
        assertEquals(Coord(1, -1), AreaVec2(2, 0).toCoord())
        assertEquals(Coord(-2, -2), AreaVec2(0, -4).toCoord())
        assertEquals(Coord(-3, -2), AreaVec2(-1, -5).toCoord())
    }

    @Test
    fun fromCoord() {
        assertEquals(AreaVec2(1, 1), Coord(1, 0).toAreaVec2())
        assertEquals(AreaVec2(2, 0), Coord(1, -1).toAreaVec2())
        assertEquals(AreaVec2(0, -4), Coord(-2, -2).toAreaVec2())
        assertEquals(AreaVec2(-1, -5), Coord(-3, -2).toAreaVec2())
    }
}

class TestStringToArea {
    @Test
    fun toArea() {
        assertEquals(Area(Coord(-2, 0).toAreaVec2(), AreaVec2(4, 4)), "0,0:1,1".toArea())
        assertEquals(Area(Coord(-9, 2).toAreaVec2(), AreaVec2(20, 20)), "1,2:-3,-4".toArea())
        assertEquals(Area(Coord(-14, 4).toAreaVec2(), AreaVec2(26, 26)), "-1,4:3,-5".toArea())
    }
}

class TestAreaOverlap {
    @Test
    fun noOverlap() {
        val a = Area(Coord(50, -50).toAreaVec2(), AreaVec2(3, 3))
        val b = Area(Coord(-100, 100).toAreaVec2(), AreaVec2(3, 3))
        assertEquals(null, a.overlap(b))
    }

    @Test
    fun fullOverlap() {
        val a = Area(Coord(13, -21).toAreaVec2(), AreaVec2(4, 5))
        val b = Area(Coord(13, -21).toAreaVec2(), AreaVec2(4, 5))
        assertEquals(Area(Coord(13, -21).toAreaVec2(), AreaVec2(4, 5)), a.overlap(b))
    }

    @Test
    fun partialOverlap() {
        var a = Area(Coord(-1, 0).toAreaVec2(), AreaVec2(2, 2))
        var b = Area(Coord(0, 0).toAreaVec2(), AreaVec2(2, 2))
        var r = Area(Coord(0, 0).toAreaVec2(), AreaVec2(1, 1))
        assertEquals(r, a.overlap(b))
        assertEquals(r, b.overlap(a))

        a = Area(Coord(-4, 1).toAreaVec2(), AreaVec2(6, 6))
        b = Area(Coord(-4, -4).toAreaVec2(), AreaVec2(10, 10))
        r = Area(AreaVec2(0, -3), AreaVec2(1, 5))
        assertEquals(r, a.overlap(b))
        assertEquals(r, b.overlap(a))
    }
}

class TestAreaPoints {
    @Test
    fun pointsOnPoint() {
        val pos = AreaVec2(1, -1)
        assertEquals(1, Area(pos, AreaVec2(0, 0)).points())
        assertEquals(1, Area(pos, AreaVec2(1, 0)).points())
        assertEquals(1, Area(pos, AreaVec2(0, 1)).points())
        assertEquals(2, Area(pos, AreaVec2(1, 1)).points())
        assertEquals(2, Area(pos, AreaVec2(2, 0)).points())
        assertEquals(2, Area(pos, AreaVec2(0, 2)).points())
        assertEquals(3, Area(pos, AreaVec2(2, 1)).points())
        assertEquals(3, Area(pos, AreaVec2(1, 2)).points())
        assertEquals(5, Area(pos, AreaVec2(2, 2)).points())
        assertEquals(13, Area(pos, AreaVec2(4, 4)).points())
    }

    @Test
    fun pointsNotOnPoint() {
        val pos = AreaVec2(1, 0)
        assertEquals(0, Area(pos, AreaVec2(0, 0)).points())
        assertEquals(1, Area(pos, AreaVec2(1, 0)).points())
        assertEquals(1, Area(pos, AreaVec2(0, 1)).points())
        assertEquals(2, Area(pos, AreaVec2(1, 1)).points())
        assertEquals(1, Area(pos, AreaVec2(2, 0)).points())
        assertEquals(1, Area(pos, AreaVec2(0, 2)).points())
        assertEquals(3, Area(pos, AreaVec2(2, 1)).points())
        assertEquals(3, Area(pos, AreaVec2(1, 2)).points())
        assertEquals(4, Area(pos, AreaVec2(2, 2)).points())
        assertEquals(12, Area(pos, AreaVec2(4, 4)).points())
    }
}

class TestAreaPointsInRect {
    @Test
    fun areaFullyInsideRegion() {
        var pos = AreaVec2(1, -1)
        assertEquals(1, Area(pos, AreaVec2(0, 0)).pointsOnAndAboveX(-100))
        assertEquals(1, Area(pos, AreaVec2(1, 0)).pointsOnAndAboveX(-100))
        assertEquals(1, Area(pos, AreaVec2(0, 1)).pointsOnAndAboveX(-100))
        assertEquals(2, Area(pos, AreaVec2(1, 1)).pointsOnAndAboveX(-100))
        assertEquals(2, Area(pos, AreaVec2(2, 0)).pointsOnAndAboveX(-100))
        assertEquals(2, Area(pos, AreaVec2(0, 2)).pointsOnAndAboveX(-100))
        assertEquals(3, Area(pos, AreaVec2(2, 1)).pointsOnAndAboveX(-100))
        assertEquals(3, Area(pos, AreaVec2(1, 2)).pointsOnAndAboveX(-100))
        assertEquals(5, Area(pos, AreaVec2(2, 2)).pointsOnAndAboveX(-100))
        assertEquals(13, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(-100))

        pos = AreaVec2(1, 0)
        assertEquals(0, Area(pos, AreaVec2(0, 0)).pointsOnAndAboveX(-100))
        assertEquals(1, Area(pos, AreaVec2(1, 0)).pointsOnAndAboveX(-100))
        assertEquals(1, Area(pos, AreaVec2(0, 1)).pointsOnAndAboveX(-100))
        assertEquals(2, Area(pos, AreaVec2(1, 1)).pointsOnAndAboveX(-100))
        assertEquals(1, Area(pos, AreaVec2(2, 0)).pointsOnAndAboveX(-100))
        assertEquals(1, Area(pos, AreaVec2(0, 2)).pointsOnAndAboveX(-100))
        assertEquals(3, Area(pos, AreaVec2(2, 1)).pointsOnAndAboveX(-100))
        assertEquals(3, Area(pos, AreaVec2(1, 2)).pointsOnAndAboveX(-100))
        assertEquals(4, Area(pos, AreaVec2(2, 2)).pointsOnAndAboveX(-100))
        assertEquals(12, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(-100))
    }

    @Test
    fun equalSidedAreaPartiallyOverlapsRegion() {
        val pos = AreaVec2(0, 0)
        assertEquals(13, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(-1))
        assertEquals(13, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(0))
        assertEquals(12, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(1))
        assertEquals(9, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(2))
        assertEquals(4, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(3))
        assertEquals(1, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(4))
        assertEquals(0, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(5))
        assertEquals(0, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(6))
    }

    @Test
    fun equalSidedAreaPartiallyOverlapsRegionOffset() {
        val pos = AreaVec2(-2, 3)
        assertEquals(12, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(0))
        assertEquals(12, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(1))
        assertEquals(10, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(2))
        assertEquals(6, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(3))
        assertEquals(2, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(4))
        assertEquals(0, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(5))
        assertEquals(0, Area(pos, AreaVec2(4, 4)).pointsOnAndAboveX(6))
    }

    @Test
    fun notEqualSidedAreaPartiallyOverlapsRegion() {
        val pos = AreaVec2(0, 0)
        assertEquals(18, Area(pos, AreaVec2(6, 4)).pointsOnAndAboveX(0))
        assertEquals(17, Area(pos, AreaVec2(6, 4)).pointsOnAndAboveX(1))
        assertEquals(14, Area(pos, AreaVec2(6, 4)).pointsOnAndAboveX(2))
        assertEquals(9, Area(pos, AreaVec2(6, 4)).pointsOnAndAboveX(3))
        assertEquals(4, Area(pos, AreaVec2(6, 4)).pointsOnAndAboveX(4))
        assertEquals(1, Area(pos, AreaVec2(6, 4)).pointsOnAndAboveX(5))
        assertEquals(0, Area(pos, AreaVec2(6, 4)).pointsOnAndAboveX(6))

        assertEquals(39, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(0))
        assertEquals(38, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(1))
        assertEquals(35, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(2))
        assertEquals(30, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(3))
        assertEquals(23, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(4))
        assertEquals(16, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(5))
        assertEquals(9, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(6))
        assertEquals(4, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(7))
        assertEquals(1, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(8))
        assertEquals(0, Area(pos, AreaVec2(6, 10)).pointsOnAndAboveX(9))
    }

    @Test
    fun bigTest() {
        var prev: Long? = null
        var prevHalf: Long? = null
        for (v in 1000..100000000 step 1000) {
            val pos = AreaVec2(-v, 0)
            val allPoints = Area(pos, AreaVec2(v * 2, v * 2)).points()

            if (prev == null) { prev = allPoints }
            assert(allPoints >= prev)

            val halfPoints = Area(pos, AreaVec2(v * 2, v * 2)).pointsOnAndAboveX(0)
            if (prevHalf == null) { prevHalf = halfPoints }
            assert(halfPoints >= prevHalf)
        }
    }
}
