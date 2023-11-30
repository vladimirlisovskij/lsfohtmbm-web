package tech.lsfohtmbm.source.database.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.ArticlePreview
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.database.api.DataBaseSource
import tech.lsfohtmbm.source.database.impl.database.Database
import tech.lsfohtmbm.source.database.impl.mapper.DateMapper
import tech.lsfohtmbm.source.database.impl.mapper.ParagraphMapper

internal class SqlDelightDataBaseSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val database: Database,
    private val paragraphMapper: ParagraphMapper,
    private val dateMapper: DateMapper
) : DataBaseSource {
    override suspend fun getArticlePreviews(): Previews {
        return withContext(ioDispatcher) {
            database.articlesQueries
                .getPreview { id, date, title ->
                    ArticlePreview(
                        id = id,
                        title = title,
                        date = dateMapper.mapToDateWrapper(date)
                    )
                }.executeAsList()
                .let { Previews(it) }
        }
    }

    override suspend fun getArticle(id: Long): Article? {
        return withContext(ioDispatcher) {
            database.articlesQueries.get(id) { date, title, paragraphs ->
                Article(
                    id = id,
                    title = title,
                    date = dateMapper.mapToDateWrapper(date),
                    paragraphs = paragraphMapper.mapToParagraphList(paragraphs)
                )
            }.executeAsOneOrNull()
        }
    }

    override suspend fun deleteArticle(id: Long) {
        withContext(ioDispatcher) {
            database.articlesQueries.delete(id)
        }
    }

    override suspend fun insertArticle(article: Article): Long {
        return withContext(ioDispatcher) {
            val date = dateMapper.mapToLong(article.date)
            val paragraphs = paragraphMapper.mapToString(article.paragraphs)
            val id = if (article.id == -1L) null else article.id
            database.articlesQueries.insert(
                id,
                date,
                article.title,
                paragraphs
            ).executeAsOne()
        }
    }

    override suspend fun getArticleImage(id: Long): ByteArray? {
        return withContext(ioDispatcher) {
            database.imagesQueries.get(id).executeAsOneOrNull()
        }
    }
}
