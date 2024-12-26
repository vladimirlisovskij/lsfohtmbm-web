package tech.lsfohtmbm.server.storage.routing

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Attachment
import tech.lsfohtmbm.source.database.api.DataBaseSource

internal fun Routing.configureAttachmentRouting(
    dataBaseSource: DataBaseSource,
) {
    route("/${StorageApi.ENDPOINT_ATTACHMENTS_ATTACHMENT}") {
        get {
            val isFinished = call.request.queryParameters[StorageApi.PARAM_IS_FINISHED]
                ?.toBooleanStrictOrNull()

            val id = call.request.queryParameters[StorageApi.PARAM_ID]
                ?.toLongOrNull()

            if (isFinished == null || id == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val attachment = dataBaseSource.getAttachment(id, isFinished)
                if (attachment != null) {
                    call.respondBytes(
                        attachment.data,
                        ContentType.parse(attachment.type),
                        HttpStatusCode.OK
                    )
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        delete {
            val isSuccess = call.receiveParameters()[StorageApi.PARAM_ID]
                ?.toLongOrNull()
                ?.let { dataBaseSource.deleteAttachment(it) } == true

            val status = if (isSuccess) HttpStatusCode.OK else HttpStatusCode.BadRequest
            call.respond(status)
        }

        put {
            var attachment: ByteArray? = null
            var attachmentType: ContentType? = null
            var articleId: Long? = null
            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        attachment = part.streamProvider().readAllBytes()
                        attachmentType = part.contentType
                    }

                    is PartData.FormItem -> {
                        when (part.name) {
                            StorageApi.PARAM_ARTICLE_ID -> articleId = part.value.toLongOrNull()
                        }
                    }

                    else -> {}
                }

                part.dispose()
            }

            if (
                attachment == null ||
                (attachmentType == null || !attachmentType!!.match(ContentType.Any)) ||
                articleId == null
            ) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val result = dataBaseSource.insertAttachment(
                    articleId!!,
                    Attachment(attachment!!, attachmentType!!.toString())
                )

                if (result != null) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        patch {
            var attachment: ByteArray? = null
            var attachmentType: ContentType? = null
            var attachmentId: Long? = null
            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        attachment = part.streamProvider().readAllBytes()
                        attachmentType = part.contentType
                    }

                    is PartData.FormItem -> {
                        when (part.name) {
                            StorageApi.PARAM_ID -> attachmentId = part.value.toLongOrNull()
                        }
                    }

                    else -> {}
                }

                part.dispose()
            }

            if (
                attachment == null ||
                (attachmentType == null || !attachmentType!!.match(ContentType.Any)) ||
                attachmentId == null
            ) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                val isSuccess = dataBaseSource.updateAttachment(
                    attachmentId!!,
                    Attachment(attachment!!, attachmentType!!.toString())
                )

                if (isSuccess) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}
