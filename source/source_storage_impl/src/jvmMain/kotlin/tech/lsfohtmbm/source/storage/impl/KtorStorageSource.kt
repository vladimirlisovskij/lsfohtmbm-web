package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.storage.api.StorageSource

internal class KtorStorageSource(
    private val client: HttpClient,
    private val baseUrl: String
) : StorageSource {
    override suspend fun getArticlePreviews(): Previews {
        return client
            .get("$baseUrl/${StorageApi.ENDPOINT_PREVIEWS}")
            .body()
    }

    override suspend fun getArticle(id: Long): Article? {
        return client
            .get("$baseUrl/${StorageApi.ENDPOINT_ARTICLE}") {
                url.parameters.append("id", id.toString())
            }.body()
    }

    override suspend fun deleteArticle(id: Long): Boolean {
        return runCatching {
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
        return runCatching {
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