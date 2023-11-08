package tech.lsfohtmbm.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.HTML
import tech.lsfohtmbm.article.Article
import tech.lsfohtmbm.article.ArticlePreview
import tech.lsfohtmbm.databasesource.api.DataBaseSource

class Server(
    private val dataBaseSource: DataBaseSource,
    private val mainPage: HTML.() -> Unit,
    private val articlesListPage: HTML.(List<ArticlePreview>) -> Unit,
    private val articleRenderer: HTML.(Article) -> Unit
) {
    fun run() {
        embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
            routing {
                get("/") {
                    call.respondHtml(HttpStatusCode.OK, mainPage)
                }

                get("/articles") {
                    val articles = dataBaseSource.getArticlePreviews()
                    call.respondHtml(HttpStatusCode.OK) { articlesListPage(articles) }
                }

                get("/article/{id}") {
                    val article = call.parameters["id"]
                        ?.toLongOrNull()
                        ?.let { dataBaseSource.getArticle(it) }

                    if (article != null) {
                        call.respondHtml(HttpStatusCode.OK) { articleRenderer(article) }
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                staticResources("/static", null)
            }
        }.start(wait = true)
    }
}
