package tech.lsfohtmbm.source.database.impl

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import tech.lsfohtmbm.source.database.impl.database.ArticlesQueries
import tech.lsfohtmbm.source.database.impl.database.Database
import kotlin.properties.Delegates
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private const val MOCK_DATE = 0L
private const val MOCK_NAME = "MOCK_NAME"
private const val MOCK_TEXT = "MOCK_TEXT"

private const val UPDATED_DATE = 1L
private const val UPDATED_NAME = "UPDATED_NAME"
private const val UPDATED_TEXT = "UPDATED_TEXT"

class RequestsTests {
    private var database by Delegates.notNull<Database>()

    @BeforeEach
    fun initEmptyDatabase() {
        database = createTestDatabase()
    }

    @Test
    fun `previews sorting`() {
        repeat(3) {
            database
                .articlesQueries
                .insertMockArticle(date = it.toLong())
                .executeAsOne()
        }

        val extractedArticlesIds = database.articlesQueries
            .getPreview { id, _, _ -> id }
            .executeAsList()

        val sortedIds = extractedArticlesIds.sortedByDescending { it }
        assertContentEquals(sortedIds, extractedArticlesIds)
    }

    @Test
    fun `insertion and extraction`() {
        val insertedArticleId = database.articlesQueries
            .insertMockArticle()
            .executeAsOne()

        val extractedArticle = database.articlesQueries
            .get(insertedArticleId)
            .executeAsOne()

        assertAll(
            "inserted entry properties",
            { assertEquals(MOCK_DATE, extractedArticle.date) },
            { assertEquals(MOCK_TEXT, extractedArticle.text) },
            { assertEquals(MOCK_NAME, extractedArticle.name) }
        )

        val updatedArticleId = database.articlesQueries
            .insertMockArticle(insertedArticleId, UPDATED_DATE, UPDATED_NAME, UPDATED_TEXT)
            .executeAsOne()

        assertEquals(insertedArticleId, updatedArticleId)

        val extractedUpdatedArticle = database.articlesQueries
            .get(updatedArticleId)
            .executeAsOne()

        assertAll(
            "updated entry properties",
            { assertEquals(UPDATED_DATE, extractedUpdatedArticle.date) },
            { assertEquals(UPDATED_TEXT, extractedUpdatedArticle.text) },
            { assertEquals(UPDATED_NAME, extractedUpdatedArticle.name) }
        )
    }

    @Test
    fun deletion() {
        val insertedArticleId = database.articlesQueries
            .insertMockArticle()
            .executeAsOne()

        val extractedArticle = database.articlesQueries
            .get(insertedArticleId)
            .executeAsOneOrNull()

        assertNotNull(extractedArticle)
        database.articlesQueries.delete(insertedArticleId)
        val extractedArticleAfterDeletion = database.articlesQueries
            .get(insertedArticleId)
            .executeAsOneOrNull()

        assertNull(extractedArticleAfterDeletion)
    }

    private fun ArticlesQueries.insertMockArticle(
        id: Long? = null,
        date: Long = MOCK_DATE,
        name: String = MOCK_NAME,
        text: String = MOCK_TEXT
    ): ExecutableQuery<Long> {
        return insert(id, date, name, text)
    }

    private fun createTestDatabase(): Database {
        return createDatabase(createTestDriver())
    }

    private fun createTestDriver(): JdbcSqliteDriver {
        return JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
            schema = Database.Schema
        )
    }
}
