package tech.lsfohtmbm.source.database.api

import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.Previews

interface DataBaseSource {
    suspend fun getArticlePreviews(): Previews

    suspend fun getArticle(id: Long): Article?

    suspend fun deleteArticle(id: Long)

    suspend fun insertArticle(article: Article): Long

    suspend fun getArticleImage(id: Long): ByteArray?
}
