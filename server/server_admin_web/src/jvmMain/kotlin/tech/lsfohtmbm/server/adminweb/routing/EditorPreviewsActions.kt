package tech.lsfohtmbm.server.adminweb.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import tech.lsfohtmbm.api.adminweb.AdminWebApi
import tech.lsfohtmbm.source.storage.api.StorageSource

internal fun Routing.configureEditorPreviewsActions(
    storage: StorageSource,
) {
    route("/${AdminWebApi.ENDPOINT_PREVIEWS}") {
        get {
            val previews = storage.getPreviews()
            if (previews != null) {
                call.respond(HttpStatusCode.OK, previews)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
