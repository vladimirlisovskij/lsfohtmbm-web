package tech.lsfohtmbm.articlerepository.api

import tech.lsfohtmbm.article.Article

interface ArticleRepository {
    fun getDynamicArticle(id: Long): Article
}
