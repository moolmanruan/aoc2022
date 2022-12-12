import factors.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class FactorsTest() {
    @Test
    fun primes() {
        assertEquals(listOf(2), primeFactors(2))
        assertEquals(listOf(3), primeFactors(3))
        assertEquals(listOf(5), primeFactors(5))
        assertEquals(listOf(13), primeFactors(13))
    }

    @Test
    fun notPrimes() {
        assertEquals(listOf(2, 2), primeFactors(4))
        assertEquals(listOf(2, 2, 5), primeFactors(20))
        assertEquals(listOf(2, 3, 5), primeFactors(30))
        assertEquals(listOf(7, 11), primeFactors(77))
    }
}

class LCMTest() {
    @Test
    fun sameValues() {
        assertEquals(2, leastCommonMultiple(2, 2))
        assertEquals(11, leastCommonMultiple(11, 11))
    }

    @Test
    fun fullMultiple() {
        assertEquals(6, leastCommonMultiple(2, 3))
        assertEquals(55, leastCommonMultiple(11, 5))
    }

    @Test
    fun lowerThanFullMultiple() {
        assertEquals(60, leastCommonMultiple(20, 30))
        assertEquals(574, leastCommonMultiple(14, 82))
    }
}
