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

fun main(arguments: Array<String>) {
    val (_, dataBasePath) = arguments
        .first { it.startsWith(ARG_DATABASE_PATH) }
        .split("=")

    val (_, host) = arguments
        .first { it.startsWith(ARG_HOST) }
        .split("=")

    val (_, port) = arguments
        .first { it.startsWith(ARG_PORT) }
        .split("=")

    Server(
        createDataBaseSource(dataBasePath),
        HTML::mainPage,
        HTML::articleListPage,
        HTML::renderArticle,
        host,
        port.toInt()
    ).run()
}
