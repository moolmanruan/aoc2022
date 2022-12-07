data class File(val name: String, val size: Int)
data class Directory(val name: String, val parent: Directory?, val dirs: MutableList<Directory>, val files: MutableList<File>)

fun Directory.size(): Int {
    return this.files.sumOf { it.size } + this.dirs.sumOf { it.size() }
}

fun newDir(name: String, parent: Directory?): Directory {
    return Directory(
        name,
        parent,
        emptyList<Directory>().toMutableList(),
        emptyList<File>().toMutableList()
    )
}

fun changeDir(pwd: Directory, to: String): Directory {
    return when (to) {
        ".." -> pwd.parent ?: pwd
        "/" -> {
            var p: Directory = pwd
            while (p.parent != null) {
                p = p.parent!!
            }
            p
        }
        else -> pwd.dirs.find { it.name == to } ?: pwd
    }
}

fun listDir(pwd: Directory, cmd: String): Directory {
    val lines = cmd.split("\n").map(String::trim)
    for (line in lines.subList(1, lines.size)) {
        val parts = line.split(" ")
        when (parts[0]) {
            "dir" -> pwd.dirs.add(newDir(parts[1], pwd))
            else -> pwd.files.add(File(parts[1], parts[0].toInt()))
        }
    }
    return pwd
}

const val totalAvailable = 70000000
const val freeSpaceRequired = 30000000

fun day7(input: String): String {
    val root = newDir("/", null)
    var pwd = root

    val cmds = input.split("$").map(String::trim).filter(String::isNotEmpty)

    for (cmd in cmds) {
        val parts = cmd.split("\n").first().split(" ")
        when (parts[0]) {
            "cd" -> pwd = changeDir(pwd, parts[1])
            "ls" -> pwd = listDir(pwd, cmd)
        }
    }

    val dirSizes = ArrayList<Int>()
    fun addDirSizes(dir: Directory) {
        dirSizes.add(dir.size())
        dir.dirs.forEach { addDirSizes(it) }
    }
    addDirSizes(root)

    println("Part one: $ANSI_BLUE${dirSizes.filter { it < 100000 }.sum()}$ANSI_RESET")

    val requiredToDelete = freeSpaceRequired - (totalAvailable - root.size())
    println("Required space: $ANSI_YELLOW$requiredToDelete$ANSI_RESET")

    return dirSizes.filter { it >= requiredToDelete }.sorted().first().toString()
}
