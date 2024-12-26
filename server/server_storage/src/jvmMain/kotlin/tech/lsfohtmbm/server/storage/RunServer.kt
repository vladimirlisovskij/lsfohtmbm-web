package tech.lsfohtmbm.server.storage

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import tech.lsfohtmbm.server.storage.routing.configureArticlesRouting
import tech.lsfohtmbm.server.storage.routing.configurePreviewsRouting
import tech.lsfohtmbm.server.storage.routing.configureAttachmentRouting
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
        configurePreviewsRouting(dataBaseSource)
        configureArticlesRouting(dataBaseSource)
        configureAttachmentRouting(dataBaseSource)
    }
}

private fun Application.configureNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}
