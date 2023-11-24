package tech.lsfohtmbm.source.storage.api

import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews

interface StorageSource {
    suspend fun getArticlePreviews(): Previews

    suspend fun getArticle(id: Long): Article?

    suspend fun deleteArticle(id: Long): Boolean

    suspend fun insertArticle(article: Article): InsertionResult?

    suspend fun getArticleImage(id: Long) : ByteArray?
}