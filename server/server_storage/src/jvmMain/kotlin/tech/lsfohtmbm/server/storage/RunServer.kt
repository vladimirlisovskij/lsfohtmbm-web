package tech.lsfohtmbm.server.storage

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receiveNullable
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.source.database.api.DataBaseSource

fun runServer(
    dataBaseSource: DataBaseSource,
    port: Int,
    host: String
) {
    embeddedServer(
        Netty,
        applicationEngineEnvironment {
            configureEnvironment(dataBaseSource, port, host)
        }
    ).start(wait = true)
}

internal fun ApplicationEngineEnvironmentBuilder.configureEnvironment(
    dataBaseSource: DataBaseSource,
    port: Int,
    host: String
) {
    connector {
        this.port = port
        this.host = host
    }

    module {
        configureNegotiation()
        configureRouting(dataBaseSource)
    }
}

private fun Application.configureRouting(
    dataBaseSource: DataBaseSource,
) {
    routing {
        get("/${StorageApi.ENDPOINT_PREVIEWS}") {
            call.respond(HttpStatusCode.OK, dataBaseSource.getArticlePreviews())
        }

        get("/${StorageApi.ENDPOINT_ARTICLE}") {
            val article = call.request.queryParameters[StorageApi.PARAM_ID]
                ?.toLongOrNull()
                ?.let { dataBaseSource.getArticle(it) }

            if (article != null) {
                call.respond(HttpStatusCode.OK, article)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/${StorageApi.ENDPOINT_DELETE}") {
            val id = call.receiveParameters()[StorageApi.PARAM_ID]
                ?.toLongOrNull()

            if (id != null) {
                dataBaseSource.deleteArticle(id)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/${StorageApi.ENDPOINT_INSERT}") {
            val article = call.receiveNullable<Article>()
            if (article != null) {
                call.respond(
                    HttpStatusCode.OK,
                    InsertionResult(dataBaseSource.insertArticle(article))
                )
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/${StorageApi.ENDPOINT_IMAGE}") {
            val image = call.request.queryParameters[StorageApi.PARAM_ID]
                ?.toLongOrNull()
                ?.let { dataBaseSource.getArticleImage(it) }

            if (image != null) {
                call.respondBytes(
                    image,
                    ContentType.parse("image/webp"),
                    HttpStatusCode.OK
                )
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

private fun Application.configureNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}
