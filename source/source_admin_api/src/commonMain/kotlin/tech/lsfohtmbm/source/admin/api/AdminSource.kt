package tech.lsfohtmbm.source.admin.api

import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews

interface AdminSource {
    suspend fun getPreviews(): Previews?

    suspend fun getArticle(id: Long): Article?

    suspend fun delete(id: Long): Boolean

    suspend fun insert(article: Article): InsertionResult?
}
