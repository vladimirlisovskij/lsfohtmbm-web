package tech.lsfohtmbm.server.storage.routing

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
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.source.database.api.DataBaseSource


internal fun Routing.configureArticlesRouting(
    dataBaseSource: DataBaseSource,
) {
    route("/${StorageApi.ENDPOINT_ARTICLES_ARTICLE}") {
        get {
            val isFinished = call.request.queryParameters[StorageApi.PARAM_IS_FINISHED]
                ?.toBooleanStrictOrNull()

            val id = call.request.queryParameters[StorageApi.PARAM_ID]
                ?.toLongOrNull()

            when {
                isFinished == null || id == null -> {
                    call.respond(HttpStatusCode.BadRequest)
                }

                isFinished -> {
                    val article = dataBaseSource.getFinishedArticle(id)
                    if (article != null) {
                        call.respond(HttpStatusCode.OK, article)
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }

                else -> {
                    val article = dataBaseSource.getArticle(id)
                    if (article != null) {
                        call.respond(HttpStatusCode.OK, article)
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }

        delete {
            val isSuccess = call.receiveParameters()[StorageApi.PARAM_ID]
                ?.toLongOrNull()
                ?.let { dataBaseSource.deleteArticle(it) } == true

            val status = if (isSuccess) HttpStatusCode.OK else HttpStatusCode.BadRequest
            call.respond(status)
        }

        put {
            val result = call
                .receiveNullable<Article>()
                ?.let { dataBaseSource.insertArticle(it) }

            if (result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
