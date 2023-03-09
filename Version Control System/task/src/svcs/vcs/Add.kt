package svcs.vcs

object Add : Command {
    fun parseCommand(input: Array<String>): String {
        val trackedFile by lazy { Command.resolveSibling(input.last()) }
        return when {
            input.size < 2 -> Command
                .indexFile
                .readLines()
                .asSequence()
                .mapIndexed { index, string ->
                    if (index == 0) "Tracked files:\n$string" else string
                }
                .joinToString(separator = "\n")
                .ifEmpty { "Add a file to the index." }

            !trackedFile.exists() -> "Can't find '${trackedFile.name}'."

            else -> Command
                .indexFile
                .readLines()
                .firstOrNull { it == trackedFile.name }
                .orEmpty()
                .ifEmpty { Command.indexFile.appendText("${trackedFile.name}\n") }
                .let { "The file '${trackedFile.name}' is tracked." }
        }
    }
}