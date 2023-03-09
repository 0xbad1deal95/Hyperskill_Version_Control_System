package svcs.vcs

import java.io.File
import java.security.MessageDigest

sealed interface Command {
    companion object {
        private const val HASHING_ALGORITHM = "SHA-256"

        private val vcsDirectory by lazy {
            val workingDir = System.getProperty("user.dir")
            File("$workingDir${File.separator}vcs").also { if (!it.exists()) it.mkdir() }
        }

        private val commandManual = mapOf(
            "config" to "Get and set a username.",
            "add" to "Add a file to the index.",
            "log" to "Show commit logs.",
            "commit" to "Save changes.",
            "checkout" to "Restore a file.",
        )

        val messageDigest: MessageDigest get() = MessageDigest.getInstance(HASHING_ALGORITHM)

        val vcsParentDir: File by lazy { vcsDirectory.parentFile }
        val commitsDir by lazy { resolveFile("commits") }
        val configFile by lazy { resolveFile("config.txt") }
        val indexFile by lazy { resolveFile("index.txt") }
        val logFile by lazy { resolveFile("log.txt") }

        fun resolveFile(
            filePath: String,
            relativeTo: File = vcsDirectory,
        ): File = relativeTo
            .resolve(filePath)
            .also {
                when {
                    !it.exists() && it.name.endsWith(".txt") -> it.createNewFile()
                    !it.exists() -> it.mkdir()
                }
            }

        fun resolveSibling(
            filePath: String,
            relativeTo: File = vcsDirectory,
        ): File = relativeTo.resolveSibling(filePath)

        fun parseInput(input: Array<String>): String = when (input.firstOrNull().orEmpty()) {
            "", "--help" -> commandManual
                .map { (key, value) -> "${key.padEnd(8, ' ')}  $value" }
                .joinToString(separator = "\n", prefix = "These are SVCS commands:\n")

            "add" -> Add.parseCommand(input)

            "commit" -> Commit.parseCommand(input)

            "log" -> Log.parseCommand()

            "config" -> Config.parseCommand(input)

            "checkout" -> Checkout.parseCommand(input)

            else -> "'${input.first()}' is not a SVCS command."
        }
    }
}