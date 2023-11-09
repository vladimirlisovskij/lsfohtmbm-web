package tech.lsfohtmbm.app

import kotlinx.html.HTML
import tech.lsfohtmbm.articlelistpage.articleListPage
import tech.lsfohtmbm.articlerenderer.renderArticle
import tech.lsfohtmbm.databasesource.impl.createDataBaseSource
import tech.lsfohtmbm.mainpage.mainPage
import tech.lsfohtmbm.server.Server

private const val ARG_DATABASE_PATH = "--database"
private const val ARG_HOST = "--host"
private const val ARG_PORT = "--port"

private fun Array<String>.getArgument(argumentName: String): String? {
    val argument = firstOrNull { it.startsWith(argumentName) } ?: return null
    return runCatching {
        val (_, value) = argument.split("=")
        value
    }.getOrNull()
}

fun main(arguments: Array<String>) {
    println(arguments.joinToString(separator = "+"))
    val dataBasePath = arguments.getArgument(ARG_DATABASE_PATH)
        ?: throw Exception("Unknown database path")

    val host = arguments.getArgument(ARG_HOST)
        ?: throw Exception("Unknown host")

    val port = arguments.getArgument(ARG_PORT)
        ?: throw Exception("Unknown port")

    Server(
        createDataBaseSource(dataBasePath),
        HTML::mainPage,
        HTML::articleListPage,
        HTML::renderArticle,
        host,
        port.toInt()
    ).run()
}
