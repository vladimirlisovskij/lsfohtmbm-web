package tech.lsfohtmbm.articlerepository.impl

import tech.lsfohtmbm.articlerepository.api.ArticleRepository
import tech.lsfohtmbm.articlerepository.impl.database.createDatabase
import tech.lsfohtmbm.articlerepository.impl.repository.ArticleRepositoryImpl

fun createArticleRepository(): ArticleRepository {
    return ArticleRepositoryImpl(
        createDatabase()
    )
}