package tech.lsfohtmbm.server.adminweb

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
import kotlinx.html.HTML
import tech.lsfohtmbm.server.adminweb.routing.configureEditorArticleActions
import tech.lsfohtmbm.server.adminweb.routing.configureEditorPreviewsActions
import tech.lsfohtmbm.server.adminweb.routing.configureRootEditorRouting
import tech.lsfohtmbm.server.adminweb.routing.configureStaticResources
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
        configureRootEditorRouting(editor)
        configureEditorArticleActions(storage)
        configureEditorPreviewsActions(storage)
        configureStaticResources()
    }
}
