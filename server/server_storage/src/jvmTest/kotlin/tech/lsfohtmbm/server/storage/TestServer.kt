package tech.lsfohtmbm.server.storage

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parametersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.ArticlePreview
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.database.api.DataBaseSource
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private const val HOST = "192.168.0.1"
private const val PORT = 8080
private const val BASE_URL = "http://$HOST:$PORT"

private const val MOCK_IMAGE_ID = 1L

class TestServer {
    private val articlePreviews = listOf(
        ArticlePreview(1L, "1", DateWrapper(1, 2, 3)),
        ArticlePreview(2L, "2", DateWrapper(1, 2, 3)),
        ArticlePreview(3L, "3", DateWrapper(1, 2, 3)),
    )

    private val mockArticle = Article(1, "title", DateWrapper(0, 0, 0), emptyList())

    private val mockImage = byteArrayOf(1, 2, 3)

    private val mockDataSource = MockDatabaseSource(
        articlePreviews,
        mockArticle,
        MOCK_IMAGE_ID,
        mockImage
    )

    @Test
    fun `get previews`() = testEnvironmentApplication {
        val response = jsonClient().get("$BASE_URL/${StorageApi.ENDPOINT_PREVIEWS}")
        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<Previews>()
        assertContentEquals(articlePreviews, receivedBody.previews)
    }

    @Test
    fun `get exist article`() = testEnvironmentApplication {
        val response = jsonClient().get("$BASE_URL/${StorageApi.ENDPOINT_ARTICLE}") {
            parameter(StorageApi.PARAM_ID, mockArticle.id.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<Article>()
        assertEquals(mockArticle, receivedBody)
    }

    @Test
    fun `get article by wrong id`() = testEnvironmentApplication {
        val response = jsonClient().get("$BASE_URL/${StorageApi.ENDPOINT_ARTICLE}") {
            parameter(StorageApi.PARAM_ID, "-1")
        }

        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `get article with wrong params`() = testEnvironmentApplication {
        val response = jsonClient().get("$BASE_URL/${StorageApi.ENDPOINT_ARTICLE}")
        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `delete article`() = testEnvironmentApplication {
        val response = jsonClient().post("$BASE_URL/${StorageApi.ENDPOINT_DELETE}") {
            setBody(
                FormDataContent(
                    parametersOf(StorageApi.PARAM_ID, "0")
                )
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `delete article with wrong params`() = testEnvironmentApplication {
        val response = jsonClient().post("$BASE_URL/${StorageApi.ENDPOINT_DELETE}") {
            contentType(ContentType.Application.FormUrlEncoded)
        }

        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `insert article`() = testEnvironmentApplication {
        val response = jsonClient().post("$BASE_URL/${StorageApi.ENDPOINT_INSERT}") {
            setBody(mockArticle)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val insertionResult = response.body<InsertionResult>()
        assertEquals(mockArticle.id, insertionResult.newArticleId)
    }

    @Test
    fun `insert article with wrong params`() = testEnvironmentApplication {
        val response = jsonClient().post("$BASE_URL/${StorageApi.ENDPOINT_INSERT}")
        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `get existed image`() = testEnvironmentApplication {
        val response = client.get("$BASE_URL/${StorageApi.ENDPOINT_IMAGE}") {
            parameter(StorageApi.PARAM_ID, MOCK_IMAGE_ID.toString())
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(mockImage, response.readBytes())
    }

    @Test
    fun `get image with wrong params`() = testEnvironmentApplication {
        val response = client.get("$BASE_URL/${StorageApi.ENDPOINT_IMAGE}")
        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    private fun ApplicationTestBuilder.jsonClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) { json() }
        }
    }

    private fun testEnvironmentApplication(
        block: suspend ApplicationTestBuilder.() -> Unit
    ) = testApplication {
        setupTestEnvironment()
        block()
    }

    private fun ApplicationTestBuilder.setupTestEnvironment() {
        environment {
            configureEnvironment(
                mockDataSource,
                PORT,
                HOST
            )
        }
    }

    private class MockDatabaseSource(
        private val previews: List<ArticlePreview>,
        private val article: Article,
        private val mockImageId: Long,
        private val mockImage: ByteArray
    ) : DataBaseSource {
        override suspend fun getArticlePreviews(): Previews {
            return Previews(previews)
        }

        override suspend fun getArticle(id: Long): Article? {
            return if (id == article.id) {
                article
            } else {
                null
            }
        }

        override suspend fun deleteArticle(id: Long) {
            // no-op
        }

        override suspend fun insertArticle(article: Article): Long {
            return article.id
        }

        override suspend fun getArticleImage(id: Long): ByteArray? {
            return if (id == mockImageId) {
                mockImage
            } else {
                null
            }
        }
    }
}
