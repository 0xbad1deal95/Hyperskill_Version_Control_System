package svcs.vcs

object Checkout : Command {
    fun parseCommand(input: Array<String>): String = when {
        input.size < 2 -> "Commit id was not passed."
        else -> Command
            .commitsDir
            .listFiles()
            ?.firstOrNull { it.name == input.last() }
            ?.also { it.copyRecursively(Command.vcsParentDir, overwrite = true) }
            ?.let { "Switched to commit ${it.name}." }
            .orEmpty()
            .ifEmpty { "Commit does not exist." }
    }
}