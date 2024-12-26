package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import tech.lsfohtmbm.source.storage.api.StorageSource

internal fun Routing.configureAttachments(
    storageSource: StorageSource,
) {
    get("/attachment/{id}") {
        val id = call.parameters["id"]
            ?.toLongOrNull()
            ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

        val attachment = storageSource
            .getAttachment(id, true)
            ?: run {
                call.respond(HttpStatusCode.InternalServerError)
                return@get
            }

        call.respondBytes(
            attachment.data,
            ContentType.parse(attachment.type),
            HttpStatusCode.OK
        )
    }
}
