package tech.lsfohtmbm.server.adminweb

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parametersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.html.HTML
import kotlinx.html.unsafe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.api.adminweb.AdminWebApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.ArticlePreview
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.storage.api.StorageSource
import tech.lsfohtmbm.utils.tests.ktor.mockHtml
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private const val HOST = "192.168.0.1"
private const val PORT = 8080
private const val BASE_URL = "http://$HOST:$PORT"

class ServerTest {
    private val previews = Previews(
        listOf(
            ArticlePreview(1L, "1", DateWrapper(1, 2, 3)),
            ArticlePreview(2L, "2", DateWrapper(1, 2, 3)),
            ArticlePreview(3L, "3", DateWrapper(1, 2, 3)),
        )
    )

    private val article = Article(
        1L,
        "title",
        DateWrapper(0, 0, 0),
        emptyList()
    )

    private val mockStorageSource = MockStorageSource(previews, article)

    @Test
    fun testMainPage() = testEnvironmentApplication {
        val response = client.get("$BASE_URL/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mockHtml { mockEditor() }, response.bodyAsText())
    }

    @Test
    fun testPreviews() = testEnvironmentApplication {
        val response = client.get("$BASE_URL/${AdminWebApi.ENDPOINT_PREVIEWS}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(response.contentType()?.contentType, ContentType.Application.Json.contentType)
        assertEquals(Json.encodeToString(previews), response.bodyAsText())
    }

    @Test
    fun testArticle() = testEnvironmentApplication {
        val response = client.get("$BASE_URL/${AdminWebApi.ENDPOINT_ARTICLE}") {
            parameter(AdminWebApi.PARAM_ID, article.id)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(response.contentType()?.contentType, ContentType.Application.Json.contentType)
        assertEquals(Json.encodeToString(article), response.bodyAsText())
    }

    @Test
    fun testArticleBadRequest() = testEnvironmentApplication {
        val response = client.get("$BASE_URL/${AdminWebApi.ENDPOINT_ARTICLE}") {
            parameter(AdminWebApi.PARAM_ID, -1)
        }

        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testArticleDelete() = testEnvironmentApplication {
        val response = client.post("$BASE_URL/${AdminWebApi.ENDPOINT_DELETE}") {
            setBody(FormDataContent(parametersOf(AdminWebApi.PARAM_ID, article.id.toString())))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testArticleDeleteBadRequest() = testEnvironmentApplication {
        val response = client.post("$BASE_URL/${AdminWebApi.ENDPOINT_DELETE}") {
            setBody(FormDataContent(parametersOf(AdminWebApi.PARAM_ID, "-1")))
        }

        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testArticleInsertion() = testEnvironmentApplication {
        val response = jsonClient().post("$BASE_URL/${AdminWebApi.ENDPOINT_INSERT}") {
            setBody(article)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(article.id, response.body<InsertionResult>().newArticleId)
    }

    @Test
    fun testArticleInsertionBadRequest() = testEnvironmentApplication {
        val response = jsonClient().post("$BASE_URL/${AdminWebApi.ENDPOINT_INSERT}") {
            setBody(article.copy(id = -1))
            contentType(ContentType.Application.Json)
        }

        assertNotEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testStaticResources() = testEnvironmentApplication {
        val testResource = client.get("$BASE_URL/static/test.resource")
        assertEquals(HttpStatusCode.OK, testResource.status)
        assertEquals("test", testResource.bodyAsText())
    }

    @Test
    fun testStaticResourcesNotFound() = testEnvironmentApplication {
        val testResource = client.get("$BASE_URL/static/123")
        assertEquals(HttpStatusCode.NotFound, testResource.status)
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
                host = HOST,
                port = PORT,
                storage = mockStorageSource,
                editor = { mockEditor() }
            )
        }
    }

    private fun HTML.mockEditor() {
        unsafe { +"mock editor" }
    }

    private class MockStorageSource(
        private val previews: Previews,
        private val article: Article
    ) : StorageSource {
        override suspend fun getArticlePreviews(): Previews {
            return previews
        }

        override suspend fun getArticle(id: Long): Article? {
            return if (id == article.id) {
                article
            } else {
                null
            }
        }

        override suspend fun deleteArticle(id: Long): Boolean {
            return id == article.id
        }

        override suspend fun insertArticle(article: Article): InsertionResult? {
            return if (article.id == this.article.id) {
                InsertionResult(article.id)
            } else {
                null
            }
        }

        override suspend fun getArticleImage(id: Long): ByteArray? {
            error("Not implemented")
        }
    }
}
