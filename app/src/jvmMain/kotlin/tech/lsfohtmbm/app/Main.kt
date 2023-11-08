package tech.lsfohtmbm.app

import kotlinx.html.HTML
import tech.lsfohtmbm.databasesource.impl.createDataBaseSource
import tech.lsfohtmbm.server.Server
import tech.lsfohtmbm.mainpage.mainPage
import tech.lsfohtmbm.articlelistpage.articleListPage
import tech.lsfohtmbm.articlerenderer.renderArticle

private const val ARG_DATABASE_PATH = "--database"

fun main(arguments: Array<String>) {
    val (_, dataBasePath) = arguments
        .first { it.startsWith(ARG_DATABASE_PATH) }
        .split("=")

    Server(
        createDataBaseSource(dataBasePath),
        HTML::mainPage,
        HTML::articleListPage,
        HTML::renderArticle
    ).run()
}
