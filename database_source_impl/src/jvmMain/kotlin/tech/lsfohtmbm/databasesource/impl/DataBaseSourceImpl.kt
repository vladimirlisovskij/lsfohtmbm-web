package tech.lsfohtmbm.databasesource.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import tech.lsfohtmbm.article.*
import tech.lsfohtmbm.databasesource.api.DataBaseSource
import tech.lsfohtmbm.databasesource.impl.database.Database
import tech.lsfohtmbm.databasesource.impl.mapper.DateMapper
import tech.lsfohtmbm.databasesource.impl.mapper.ParagraphMapper


internal class DataBaseSourceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val database: Database,
    private val paragraphMapper: ParagraphMapper,
    private val dateMapper: DateMapper
) : DataBaseSource {
    override suspend fun getArticlePreviews(): List<ArticlePreview> {
        return withContext(ioDispatcher) {
            database.articlesQueries.getPreview { id, date, title ->
                ArticlePreview(
                    id = id,
                    title = title,
                    date = dateMapper.mapToDateWrapper(date)
                )
            }.executeAsList()
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

    override suspend fun getArticleImage(id: Long): ByteArray? {
        return withContext(ioDispatcher) {
            database.imagesQueries.get(id).executeAsOneOrNull()
        }
    }
}
