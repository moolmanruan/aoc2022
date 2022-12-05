enum class Throw { ROCK, PAPER, SCISSORS }
enum class Result { LOSE, DRAW, WIN }
data class Round(val opp: Throw, val res: Result)

fun String.toRound(): Round {
    val parts = this.split(" ")
    val opp = when (parts[0]) {
        "A" -> Throw.ROCK
        "B" -> Throw.PAPER
        "C" -> Throw.SCISSORS
        else -> throw Exception("invalid throw")
    }
    val me = when (parts[1]) {
        "X" -> Result.LOSE
        "Y" -> Result.DRAW
        "Z" -> Result.WIN
        else -> throw Exception("invalid throw")
    }
    return Round(opp, me)
}

fun winThrow(r: Round): Throw {
    return when (r.res) {
        Result.LOSE -> Throw.values()[(r.opp.ordinal - 1 + 3) % 3]
        Result.DRAW -> Throw.values()[r.opp.ordinal]
        Result.WIN -> Throw.values()[(r.opp.ordinal + 1) % 3]
    }
}

// fun roundResult(r: Round): Result {
//    return when {
//        r.opp.ordinal == r.me.ordinal -> Result.DRAW
//        r.opp.ordinal == (r.me.ordinal+1)%3 -> Result.LOSE
//        else -> Result.WIN
//    }
// }

// fun day2A(input: String): String {
//    val rounds = input.split("\n").map{it.toRound()}
//    var score = 0
//    for (r in rounds) {
//        when (roundResult(r)) {
//            Result.WIN -> score+=6
//            Result.DRAW -> score+=3
//            else -> {}
//        }
//        when (r.me) {
//            Throw.ROCK -> score+=1
//            Throw.PAPER -> score+=2
//            Throw.SCISSORS -> score+=3
//        }
//    }
//    return score.toString()
// }
fun day2(input: String): String {
    val rounds = input.split("\n").map { it.toRound() }
    var score = 0
    for (r in rounds) {
        val winScore = when (r.res) {
            Result.WIN -> 6
            Result.DRAW -> 3
            else -> 0
        }
        score += winScore
        val throwScore = when (winThrow(r)) {
            Throw.ROCK -> 1
            Throw.PAPER -> 2
            Throw.SCISSORS -> 3
        }
        score += throwScore
    }
    return score.toString()
}
