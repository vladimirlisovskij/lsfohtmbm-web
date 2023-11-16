package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import tech.lsfohtmbm.entity.article.Article
import tech.lsfohtmbm.entity.article.ArticlePreview
import tech.lsfohtmbm.source.storage.api.StorageSource

internal class KtorStorageSource(
    private val client: HttpClient,
    private val host: String
) : StorageSource {
    override suspend fun getArticlePreviews(): List<ArticlePreview> {
        return client
            .get("$host/previews")
            .body()
    }

    override suspend fun getArticle(id: Long): Article? {
        return client
            .get("$host/previews") {
                url.parameters.append("id", id.toString())
            }.body()
    }

    override suspend fun getArticleImage(id: Long): ByteArray? {
        val response = client.get("$host/image") {
            url.parameters.append("id", id.toString())
        }

        return if (response.status.isSuccess()) {
            response.readBytes()
        } else {
            null
        }
    }

}