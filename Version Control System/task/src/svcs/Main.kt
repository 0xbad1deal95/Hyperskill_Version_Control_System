package svcs

import svcs.vcs.Command

fun main(args: Array<String>) {
    Command.parseInput(args).also(::println)
}