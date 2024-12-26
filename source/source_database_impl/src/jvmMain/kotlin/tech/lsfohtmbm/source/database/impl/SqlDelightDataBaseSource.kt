package tech.lsfohtmbm.source.database.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.Attachment
import tech.lsfohtmbm.entity.storage.FinishedArticle
import tech.lsfohtmbm.entity.storage.FinishedPreview
import tech.lsfohtmbm.entity.storage.FinishedPreviews
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Preview
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.database.api.DataBaseSource
import tech.lsfohtmbm.source.database.impl.database.Database
import tech.lsfohtmbm.source.database.impl.mapper.BooleanMapper
import tech.lsfohtmbm.source.database.impl.mapper.DateMapper
import tech.lsfohtmbm.source.database.impl.mapper.ParagraphMapper

internal class SqlDelightDataBaseSource(
    private val ioDispatcher: CoroutineDispatcher,
    private val database: Database,
    private val paragraphMapper: ParagraphMapper,
    private val dateMapper: DateMapper,
    private val booleanMapper: BooleanMapper
) : DataBaseSource {
    override suspend fun getFinishedPreviews(): FinishedPreviews {
        return runInIo {
            database.articlesQueries
                .getFinishedPreviews { id, date, title ->
                    FinishedPreview(
                        id = id,
                        title = title,
                        date = dateMapper.mapToDateWrapper(date)
                    )
                }.executeAsList()
                .let { FinishedPreviews(it) }
        }
    }

    override suspend fun getPreviews(): Previews {
        return runInIo {
            database.articlesQueries
                .getPreviews { id, date, title, ready ->
                    Preview(
                        id = id,
                        date = dateMapper.mapToDateWrapper(date),
                        title = title,
                        ready = booleanMapper.mapToBoolean(ready),
                    )
                }.executeAsList()
                .let { Previews(it) }
        }
    }

    override suspend fun getFinishedArticle(articleId: Long): FinishedArticle? {
        return runInIo {
            database.articlesQueries
                .getFinishedArticle(articleId) { date, title, paragraphs ->
                    FinishedArticle(
                        id = articleId,
                        title = title,
                        date = dateMapper.mapToDateWrapper(date),
                        paragraphs = paragraphMapper.mapToParagraphList(paragraphs),
                    )
                }.executeAsOneOrNull()
        }
    }

    override suspend fun getArticle(articleId: Long): Article? {
        return runInIo {
            database.articlesQueries
                .getArticle(articleId) { date, title, paragraphs, ready ->
                    Article(
                        id = articleId,
                        title = title,
                        date = dateMapper.mapToDateWrapper(date),
                        paragraphs = paragraphMapper.mapToParagraphList(paragraphs),
                        ready = booleanMapper.mapToBoolean(ready)
                    )
                }.executeAsOneOrNull()
        }
    }

    override suspend fun deleteArticle(articleId: Long): Boolean {
        return runInIo {
            database.transactionWithResult {
                database.articlesQueries
                    .deleteArticle(articleId)
                    .executeAsOneOrNull() ?: rollback(false)

                database.attachmentsQueries
                    .deleteArticleAttachments(articleId)

                true
            }
        }
    }

    override suspend fun insertArticle(article: Article): InsertionResult {
        return runInIo {
            database.articlesQueries
                .insertArticle(
                    id = article.id.takeIf { it != Article.NO_ID },
                    name = article.title,
                    date = dateMapper.mapToLong(article.date),
                    text = paragraphMapper.mapToString(article.paragraphs),
                    ready = booleanMapper.mapToLong(article.ready)
                ).executeAsOne()
                .let { InsertionResult(it) }
        }
    }

    override suspend fun getAttachment(attachmentId: Long, forFinishedArticle: Boolean): Attachment? {
        return runInIo {
            val queries = database.attachmentsQueries
            val query = if (forFinishedArticle) {
                queries.getFinishedAttachment(attachmentId) { attachment, type ->
                        Attachment(attachment, type)
                    }
            } else {
                queries.getAttachment(attachmentId) { attachment, type ->
                        Attachment(attachment, type)
                    }
            }

            query.executeAsOneOrNull()
        }
    }

    override suspend fun insertAttachment(articleId: Long, attachment: Attachment): InsertionResult? {
        return runInIo {
            database.transactionWithResult {
                val isArticleExist = database.articlesQueries
                    .isAricleExist(articleId)
                    .executeAsOne()

                if (!isArticleExist) rollback(null)
                database.attachmentsQueries
                    .insert(
                        article = articleId,
                        attachment = attachment.data,
                        type = attachment.type
                    ).executeAsOne()
                    .let { InsertionResult(it) }
            }
        }
    }

    override suspend fun updateAttachment(id: Long, attachment: Attachment): Boolean {
        return runInIo {
            database.attachmentsQueries.update(
                attachment = attachment.data,
                type = attachment.type,
                id = id
            ).executeAsOneOrNull() != null
        }
    }

    override suspend fun deleteAttachment(id: Long): Boolean {
        return runInIo {
            database.attachmentsQueries
                .deleteAttachment(id)
                .executeAsOneOrNull() != null
        }
    }
    
    private suspend fun<T> runInIo(block: suspend CoroutineScope.() -> T): T {
        return withContext(ioDispatcher, block)
    }
}
