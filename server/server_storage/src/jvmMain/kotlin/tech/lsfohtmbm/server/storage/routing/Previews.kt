package tech.lsfohtmbm.server.storage.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.source.database.api.DataBaseSource

internal fun Routing.configurePreviewsRouting(
    dataBaseSource: DataBaseSource,
) {
    get("/${StorageApi.ENDPOINT_ARTICLES_PREVIEWS}") {
        val isFinished = call.request.queryParameters[StorageApi.PARAM_IS_FINISHED]
            ?.toBooleanStrictOrNull()

        when(isFinished) {
            true -> call.respond(HttpStatusCode.OK, dataBaseSource.getFinishedPreviews())
            false -> call.respond(HttpStatusCode.OK, dataBaseSource.getPreviews())
            null -> call.respond(HttpStatusCode.BadRequest)
        }
    }
}
