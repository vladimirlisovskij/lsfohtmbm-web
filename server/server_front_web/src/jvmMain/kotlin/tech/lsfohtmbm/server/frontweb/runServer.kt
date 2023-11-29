package tech.lsfohtmbm.server.frontweb

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.HTML
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.ArticlePreview
import tech.lsfohtmbm.source.storage.api.StorageSource
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore

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
    val articlesListPage: HTML.(List<ArticlePreview>) -> Unit,
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

private fun KeyStore.load(file: File, password: CharArray) {
    FileInputStream(file).use { load(it, password) }
}

internal fun ApplicationEngineEnvironmentBuilder.configureEnvironment(
    storageSource: StorageSource,
    pageConfig: PageConfig,
    hostConfig: HostConfig
) {
    val keyStoreFile = File(hostConfig.keyStore)

    val keyStore = KeyStore
        .getInstance("JKS")
        .apply { load(keyStoreFile, hostConfig.keyStorePassword.toCharArray()) }

    connector {
        port = hostConfig.defaultPort
        host = hostConfig.host
    }

    sslConnector(
        keyStore = keyStore,
        keyAlias = hostConfig.keyAlias,
        keyStorePassword = { hostConfig.keyStorePassword.toCharArray() },
        privateKeyPassword = { hostConfig.privateKeyPassword.toCharArray() }
    ) {
        port = hostConfig.sslPort
        host = hostConfig.host
        keyStorePath = keyStoreFile
    }

    module {
        configureRouting(storageSource, pageConfig)
        configureStatusPages(pageConfig)
    }
}

private fun Application.configureRouting(
    storageSource: StorageSource,
    pageConfig: PageConfig
) {
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, pageConfig.mainPage)
        }

        get("/articles") {
            val previews = storageSource.getArticlePreviews().previews
            call.respondHtml(HttpStatusCode.OK) { pageConfig.articlesListPage.invoke(this, previews) }
        }

        get("/article/{id}") {
            val article = call.parameters["id"]
                ?.toLongOrNull()
                ?.let { storageSource.getArticle(it) }

            if (article != null) {
                call.respondHtml(HttpStatusCode.OK) { pageConfig.articleRenderer.invoke(this, article) }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/image/{id}") {
            val image = call.parameters["id"]
                ?.toLongOrNull()
                ?.let { storageSource.getArticleImage(it) }

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

        staticResources("/static", null)
    }
}

private fun Application.configureStatusPages(
    pageConfig: PageConfig
) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondHtml {
                pageConfig.errorPage.invoke(this, 500)
            }
        }

        status(
            HttpStatusCode.NotFound,
            HttpStatusCode.BadRequest,
            HttpStatusCode.MethodNotAllowed,
            HttpStatusCode.InternalServerError,
            HttpStatusCode.ServiceUnavailable,
        ) { call, status ->
            val uri = call.request.uri
            if (
                !uri.startsWith("/image") &&
                !uri.startsWith("/static")
            ) {
                call.respondHtml { pageConfig.errorPage.invoke(this, status.value) }
            }
        }
    }
}
