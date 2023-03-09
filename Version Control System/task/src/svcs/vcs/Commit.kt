package svcs.vcs

object Commit : Command {
    fun parseCommand(input: Array<String>): String {
        val trackedFiles by lazy {
            Command
                .indexFile
                .readLines()
                .asSequence()
                .map { Command.resolveSibling(it) }
                .filter { it.name.endsWith(".txt") }
        }

        val messageDigest = Command.messageDigest

        val latestCommitId by lazy {
            Command
                .logFile
                .readLines()
                .asSequence()
                .chunked(3)
                .map { it.first().substringAfter(" ") }
                .lastOrNull()
                .orEmpty()
        }

        val commitId by lazy {
            trackedFiles
                .map { it.readBytes() }
                .forEach(messageDigest::update)

            messageDigest.digest().joinToString("") { it.toUByte().toString(radix = 16) }
        }

        val logText by lazy {
            """
            |commit $commitId
            |Author: ${Command.configFile.readLines().first().trim()}
            |${input.component2()}
            |""".trimMargin()
        }

        return when {
            input.size < 2 -> "Message was not passed."
            latestCommitId == commitId -> "Nothing to commit."
            else -> {
                val commitIdDir = Command.resolveFile(commitId, Command.commitsDir)
                trackedFiles.forEach { it.copyTo(Command.resolveFile(it.name, commitIdDir), overwrite = true) }
                Command.logFile.appendText(logText)
                "Changes are committed."
            }
        }
    }
}