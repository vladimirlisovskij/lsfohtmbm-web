package tech.lsfohtmbm.server.frontweb

import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.HTML
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.FinishedPreviews
import tech.lsfohtmbm.server.frontweb.connectors.createHttpConnector
import tech.lsfohtmbm.server.frontweb.connectors.createHttpsConnector
import tech.lsfohtmbm.server.frontweb.modules.configureRouting
import tech.lsfohtmbm.server.frontweb.modules.configureStatusPages
import tech.lsfohtmbm.source.storage.api.StorageSource

class HostConfig(
    val host: String,
    val defaultPort: Int,
    val sslPort: Int,
    val keyStore: String,
    val keyStorePassword: String,
    val keyAlias: String,
    val privateKeyPassword: String
)

class PageConfig(
    val mainPage: HTML.() -> Unit,
    val articlesListPage: HTML.(FinishedPreviews) -> Unit,
    val articleRenderer: HTML.(Article) -> Unit,
    val errorPage: HTML.(Int) -> Unit
)

fun runServer(
    storageSource: StorageSource,
    pageConfig: PageConfig,
    hostConfig: HostConfig
) {
    embeddedServer(
        Netty,
        applicationEngineEnvironment {
            configureEnvironment(storageSource, pageConfig, hostConfig)
        }
    ).start(wait = true)
}

internal fun ApplicationEngineEnvironmentBuilder.configureEnvironment(
    storageSource: StorageSource,
    pageConfig: PageConfig,
    hostConfig: HostConfig
) {
    connectors.add(createHttpConnector(hostConfig))
    connectors.add(createHttpsConnector(hostConfig))
    modules.add { configureRouting(storageSource, pageConfig) }
    modules.add { configureStatusPages(pageConfig) }
}
