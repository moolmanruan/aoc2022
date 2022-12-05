fun day1(input: String): String {
    val elves: List<List<Int>> = buildList {
        for (elfText in input.split("\n\n")) {
            add(elfText.split("\n").map { it.toInt() })
        }
    }

    return elves.map { it.sum() }.sorted().takeLast(3).sum().toString()
}
