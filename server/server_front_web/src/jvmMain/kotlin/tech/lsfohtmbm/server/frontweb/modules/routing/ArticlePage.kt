package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.html.HTML
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.source.storage.api.StorageSource

internal fun Routing.configureArticlePage(
    storageSource: StorageSource,
    articleRenderer: HTML.(Article) -> Unit,
) {

    get("/article/{id}") {
        val id = call.parameters["id"]
            ?.toLongOrNull()
            ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

        val article = storageSource
            .getArticle(id)
            ?: run {
                call.respond(HttpStatusCode.InternalServerError)
                return@get
            }

        call.respondHtml(HttpStatusCode.OK) { articleRenderer.invoke(this, article) }
    }
}
