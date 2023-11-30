package tech.lsfohtmbm.server.adminweb

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receiveNullable
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
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
            val previews = storage.getArticlePreviews()
            if (previews != null) {
                call.respond(HttpStatusCode.OK, previews)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
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
