package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.storage.api.StorageSource
import tech.lsfohtmbm.utils.coroutines.cancellableRunCatching

internal class KtorStorageSource(
    private val client: HttpClient,
    private val baseUrl: String
) : StorageSource {
    override suspend fun getArticlePreviews(): Previews? {
        return cancellableRunCatching {
            client
                .get("$baseUrl/${StorageApi.ENDPOINT_PREVIEWS}")
                .body<Previews>()
        }.getOrNull()
    }

    override suspend fun getArticle(id: Long): Article? {
        return cancellableRunCatching {
            client
                .get("$baseUrl/${StorageApi.ENDPOINT_ARTICLE}") {
                    url.parameters.append("id", id.toString())
                }
                .body<Article>()
        }.getOrNull()
    }

    override suspend fun deleteArticle(id: Long): Boolean {
        return cancellableRunCatching {
            client.post("$baseUrl/${StorageApi.ENDPOINT_DELETE}") {
                setBody(
                    FormDataContent(
                        parametersOf(StorageApi.PARAM_ID, id.toString())
                    )
                )
            }.status.isSuccess()
        }.getOrDefault(false)
    }

    override suspend fun insertArticle(article: Article): InsertionResult? {
        return cancellableRunCatching {
            client.post("$baseUrl/${StorageApi.ENDPOINT_INSERT}") {
                setBody(article)
                contentType(ContentType.Application.Json)
            }.body<InsertionResult>()
        }.getOrNull()
    }

    override suspend fun getArticleImage(id: Long): ByteArray? {
        val response = client.get("$baseUrl/${StorageApi.ENDPOINT_IMAGE}") {
            url.parameters.append("id", id.toString())
        }

        return if (response.status.isSuccess()) {
            response.readBytes()
        } else {
            null
        }
    }
}
