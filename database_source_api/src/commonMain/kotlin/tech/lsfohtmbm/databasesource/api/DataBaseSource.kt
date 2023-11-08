package tech.lsfohtmbm.databasesource.api

import tech.lsfohtmbm.article.Article
import tech.lsfohtmbm.article.ArticlePreview

interface DataBaseSource {
    suspend fun getArticlePreviews(): List<ArticlePreview>

    suspend fun getArticle(id: Long): Article?

    suspend fun getArticleImage(id: Long) : ByteArray?
}