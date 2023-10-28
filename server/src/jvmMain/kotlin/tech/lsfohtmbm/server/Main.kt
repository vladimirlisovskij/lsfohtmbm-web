package tech.lsfohtmbm.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.html.HTML
import tech.lsfohtmbm.article.Article
import tech.lsfohtmbm.design.render
import tech.lsfohtmbm.mainpage.mainPage


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::mainPage)
            }

            get("/blog") {
                call.respondHtml(HttpStatusCode.OK) {
                    render(
                        Article(
                            "test",
                            emptyList()
                        )
                    )
                }
            }

            staticResources("/static", null)
        }
    }.start(wait = true)
}
