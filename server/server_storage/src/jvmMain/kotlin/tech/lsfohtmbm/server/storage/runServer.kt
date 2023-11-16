package tech.lsfohtmbm.server.storage

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
        get("/previews") {
            call.respond(HttpStatusCode.OK, dataBaseSource.getArticlePreviews())
        }

        get("/article") {
            val article = call.request.queryParameters["id"]
                ?.toLongOrNull()
                ?.let { dataBaseSource.getArticle(it) }

            if (article != null) {
                call.respond(HttpStatusCode.OK, article)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/image") {
            val image = call.request.queryParameters["id"]
                ?.toLongOrNull()
                ?.let { dataBaseSource.getArticleImage(it) }

            if (image != null) {
                call.respondBytes(
                    image,
                    ContentType.parse("image/webp"),
                    HttpStatusCode.OK
                )
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun Application.configureNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}
