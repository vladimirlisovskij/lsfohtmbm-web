package tech.lsfohtmbm.server.adminweb.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveNullable
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import tech.lsfohtmbm.api.adminweb.AdminWebApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.source.storage.api.StorageSource

internal fun Routing.configureEditorArticleActions(
    storage: StorageSource,
) {
    route("/${AdminWebApi.ENDPOINT_ARTICLE}") {
        put {
            val insertionResult = call
                .receiveNullable<Article>()
                ?.let { storage.insertArticle(it) }

            if (insertionResult != null) {
                call.respond(HttpStatusCode.OK, insertionResult)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete {
            val id = call.request.queryParameters[AdminWebApi.PARAM_ID]
                ?.toLongOrNull()

            if (id != null && storage.deleteArticle(id)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get {
            val article = call.parameters[AdminWebApi.PARAM_ID]
                ?.toLongOrNull()
                ?.let { storage.getArticle(it) }

            if (article != null) {
                call.respond(HttpStatusCode.OK, article)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
