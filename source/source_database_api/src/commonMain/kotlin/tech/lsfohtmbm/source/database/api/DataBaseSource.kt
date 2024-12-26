package tech.lsfohtmbm.source.database.api

import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.Attachment
import tech.lsfohtmbm.entity.storage.FinishedArticle
import tech.lsfohtmbm.entity.storage.FinishedPreviews
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews

interface DataBaseSource {
    suspend fun getFinishedPreviews(): FinishedPreviews

    suspend fun getPreviews(): Previews

    suspend fun getFinishedArticle(articleId: Long): FinishedArticle?

    suspend fun getArticle(articleId: Long): Article?

    suspend fun deleteArticle(articleId: Long): Boolean

    suspend fun insertArticle(article: Article): InsertionResult

    suspend fun getAttachment(attachmentId: Long, forFinishedArticle: Boolean): Attachment?

    suspend fun insertAttachment(articleId: Long, attachment: Attachment): InsertionResult?

    suspend fun updateAttachment(id: Long, attachment: Attachment): Boolean

    suspend fun deleteAttachment(id: Long): Boolean
}