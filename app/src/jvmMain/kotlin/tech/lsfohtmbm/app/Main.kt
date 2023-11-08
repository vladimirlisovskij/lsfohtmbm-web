package tech.lsfohtmbm.app

import kotlinx.html.HTML
import tech.lsfohtmbm.databasesource.impl.createDataBaseSource
import tech.lsfohtmbm.server.Server
import tech.lsfohtmbm.mainpage.mainPage
import tech.lsfohtmbm.articlelistpage.articleListPage
import tech.lsfohtmbm.articlerenderer.renderArticle

fun main() {
    Server(
        createDataBaseSource(),
        HTML::mainPage,
        HTML::articleListPage,
        HTML::renderArticle
    ).run()
}