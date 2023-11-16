package tech.lsfohtmbm.source.storage.api

import tech.lsfohtmbm.entity.article.Article
import tech.lsfohtmbm.entity.article.ArticlePreview

interface StorageSource {
    suspend fun getArticlePreviews(): List<ArticlePreview>

    suspend fun getArticle(id: Long): Article?

    suspend fun getArticleImage(id: Long) : ByteArray?
}