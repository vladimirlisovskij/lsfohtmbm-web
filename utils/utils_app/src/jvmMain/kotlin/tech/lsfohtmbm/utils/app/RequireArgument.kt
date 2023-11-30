package tech.lsfohtmbm.utils.app

fun Array<String>.requireArgument(argumentName: String): String {
    return runCatching {
        val (_, value) = first { it.startsWith(argumentName) }.split("=")
        value
    }.getOrNull() ?: throw IllegalArgumentException("No argument passed for $argumentName")
}
