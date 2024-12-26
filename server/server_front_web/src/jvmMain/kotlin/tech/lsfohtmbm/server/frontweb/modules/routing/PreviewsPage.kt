package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.html.HTML
import tech.lsfohtmbm.entity.storage.FinishedPreviews
import tech.lsfohtmbm.source.storage.api.StorageSource

internal fun Routing.configurePreviewsPage(
    storageSource: StorageSource,
    articlesListPage: HTML.(FinishedPreviews) -> Unit,
) {
    get("/articles") {
        val previews = storageSource
            .getFinishedPreviews()
            ?: run {
                call.respond(HttpStatusCode.InternalServerError)
                return@get
            }

        call.respondHtml(HttpStatusCode.OK) { articlesListPage.invoke(this, previews) }
    }
}
