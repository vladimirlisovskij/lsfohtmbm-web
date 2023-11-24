package tech.lsfohtmbm.server.adminweb

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.HTML
import tech.lsfohtmbm.api.adminweb.AdminWebApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.source.storage.api.StorageSource

fun runServer(
    host: String,
    port: Int,
    storage: StorageSource,
    editor: HTML.() -> Unit
) {
    embeddedServer(
        Netty,
        applicationEngineEnvironment { configureEnvironment(host, port, storage, editor) }
    ).start(wait = true)
}

internal fun ApplicationEngineEnvironmentBuilder.configureEnvironment(
    host: String,
    port: Int,
    storage: StorageSource,
    editor: HTML.() -> Unit
) {
    connector {
        this.host = host
        this.port = port
    }

    module {
        configureNegotiation()
        configureRouting(storage, editor)
    }
}

private fun Application.configureNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}

private fun Application.configureRouting(
    storage: StorageSource,
    editor: HTML.() -> Unit
) {
    routing {
        get("/") {
            call.respondHtml { editor() }
        }

        get("/${AdminWebApi.ENDPOINT_PREVIEWS}") {
            call.respond(HttpStatusCode.OK, storage.getArticlePreviews())
        }

        post("/${AdminWebApi.ENDPOINT_INSERT}") {
            val insertionResult = call
                .receiveNullable<Article>()
                ?.let { storage.insertArticle(it) }

            if (insertionResult != null) {
                call.respond(HttpStatusCode.OK, insertionResult)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/${AdminWebApi.ENDPOINT_DELETE}") {
            val id = call.receiveParameters()[AdminWebApi.PARAM_ID]
                ?.toLongOrNull()

            if (id != null && storage.deleteArticle(id)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/${AdminWebApi.ENDPOINT_ARTICLE}") {
            val article = call.parameters[AdminWebApi.PARAM_ID]
                ?.toLongOrNull()
                ?.let { storage.getArticle(it) }

            if (article != null) {
                call.respond(HttpStatusCode.OK, article)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        staticResources("/static", null)
    }
}
