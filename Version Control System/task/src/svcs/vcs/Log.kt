package svcs.vcs

object Log : Command {
    fun parseCommand(): String = Command
        .logFile
        .readLines()
        .chunked(3)
        .map { it.joinToString(separator = "\n") }
        .asReversed()
        .joinToString(separator = "\n\n")
        .ifEmpty { "No commits yet." }
}