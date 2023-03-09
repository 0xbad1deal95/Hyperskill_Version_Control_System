package svcs.vcs

object Config : Command {
    fun parseCommand(input: Array<String>): String {
        return if (input.size < 2) Command
            .configFile
            .readLines()
            .joinToString { "The username is $it." }
            .ifEmpty { "Please, tell me who you are." }
        else "The username is ${input.component2()}.".also {
            Command.configFile.writeText(input.component2())
        }
    }
}