fun day1(lines: List<String>): String {
    val values = lines.map { it.toIntOrNull() }
    val elves: MutableList<Int> = mutableListOf()


    var count = 0
    for (value in values) {
       if (value == null) {
           elves.add(count)
           count = 0
           continue
       }
       count+=value
    }
    if (count > 0) {
        elves.add(count)
    }

    val bestElvesSum = elves.sorted().reversed().subList(0, 3).reduce { acc, i -> acc+i  }
    return bestElvesSum.toString()
}