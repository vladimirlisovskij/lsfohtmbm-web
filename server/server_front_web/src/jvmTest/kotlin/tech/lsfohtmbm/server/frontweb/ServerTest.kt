package tech.lsfohtmbm.server.frontweb

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.html.HTML
import kotlinx.html.unsafe
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.ArticlePreview
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.source.storage.api.StorageSource
import tech.lsfohtmbm.utils.tests.ktor.mockHtml
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

private const val HOST = "192.168.0.1"
private const val DEFAULT_PORT = 8080
private const val SSL_PORT = 8443
private const val HTTP_BASE_URL = "http://$HOST:$DEFAULT_PORT"

private const val AVAILABLE_ARTICLE_ID = 1L
private const val AVAILABLE_IMAGE_ID = 1L

class ServerTest {
    private val pageConfig = PageConfig(
        mainPage = { mockMainPage() },
        articleRenderer = { mockArticleRenderer(it) },
        articlesListPage = { mockArticleListPage(it) },
        errorPage = { mockErrorPage(it) }
    )

    private val hostConfig = HostConfig(
        host = HOST,
        defaultPort = DEFAULT_PORT,
        sslPort = SSL_PORT,
        keyStore = "${System.getProperty("user.dir")}/test_keystore.jks",
        keyStorePassword = "123qwe",
        keyAlias = "test-valid1",
        privateKeyPassword = "test1"
    )

    private val articlePreviews = listOf(
        ArticlePreview(1L, "1", DateWrapper(1, 2, 3)),
        ArticlePreview(2L, "2", DateWrapper(1, 2, 3)),
        ArticlePreview(3L, "3", DateWrapper(1, 2, 3)),
    )

    private val availableArticle = Article(
        AVAILABLE_ARTICLE_ID,
        "$AVAILABLE_ARTICLE_ID",
        DateWrapper(1, 2, 3),
        emptyList()
    )

    private val availableImage = byteArrayOf(1, 2, 3)

    private val mockDataSource = MockStorageSource(
        articlePreviews,
        availableArticle,
        availableImage,
        AVAILABLE_IMAGE_ID
    )

    @Test
    fun testMainPage() = testEnvironmentApplication {
        val response = client.get("$HTTP_BASE_URL/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mockHtml { mockMainPage() }, response.bodyAsText())
    }

    @Test
    fun testArticleListPage() = testEnvironmentApplication {
        val response = client.get("$HTTP_BASE_URL/articles")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mockHtml { mockArticleListPage(articlePreviews) }, response.bodyAsText())
    }

    @Test
    fun testArticleRendererPage() = testEnvironmentApplication {
        val response = client.get("$HTTP_BASE_URL/article/$AVAILABLE_ARTICLE_ID")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mockHtml { mockArticleRenderer(availableArticle) }, response.bodyAsText())
    }

    @Test
    fun testArticleRendererEmptyPage() = testEnvironmentApplication {
        val response = client.get("$HTTP_BASE_URL/article/1234")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mockHtml { mockErrorPage(404) }, response.bodyAsText())
    }

    @Test
    fun testArticleRendererErrorArgumentPage() = testEnvironmentApplication {
        val response = client.get("$HTTP_BASE_URL/article/qwe")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mockHtml { mockErrorPage(404) }, response.bodyAsText())
    }

    @Test
    fun test404Page() = testEnvironmentApplication {
        val response = client.get("$HTTP_BASE_URL/404qweqwe")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mockHtml { mockErrorPage(404) }, response.bodyAsText())
    }

    @Test
    fun testStaticResources() = testEnvironmentApplication {
        val testResource = client.get("$HTTP_BASE_URL/static/test.resource")
        assertEquals(HttpStatusCode.OK, testResource.status)
        assertEquals("test", testResource.bodyAsText())
    }

    @Test
    fun testStaticResourcesNotFound() = testEnvironmentApplication {
        val testResource = client.get("$HTTP_BASE_URL/static/123")
        assertEquals(HttpStatusCode.NotFound, testResource.status)
    }

    @Test
    fun testImage() = testEnvironmentApplication {
        val response = client.get("$HTTP_BASE_URL/image/$AVAILABLE_IMAGE_ID")
        assertEquals(HttpStatusCode.OK, response.status)
        val receivedImage = response.readBytes()
        assertContentEquals(availableImage, receivedImage)
    }

    @Test
    fun testImageNotFound() = testEnvironmentApplication {
        val image = client.get("$HTTP_BASE_URL/image/123")
        assertEquals(HttpStatusCode.NotFound, image.status)
    }

    @Test
    fun testImageErrorArgument() = testEnvironmentApplication {
        val image = client.get("$HTTP_BASE_URL/image/qwe")
        assertEquals(HttpStatusCode.NotFound, image.status)
    }

//    @Test
//    fun testSsl() {
//        TODO()
//    }

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
                pageConfig,
                hostConfig
            )
        }
    }

    private fun HTML.mockMainPage() {
        unsafe { +"mainPage" }
    }

    private fun HTML.mockErrorPage(error: Int) {
        unsafe { +"errorPage $error" }
    }

    private fun HTML.mockArticleListPage(preview: List<ArticlePreview>) {
        preview.forEach {
            unsafe { +it.toString() }
        }
    }

    private fun HTML.mockArticleRenderer(article: Article) {
        unsafe { +article.toString() }
    }

    private class MockStorageSource(
        private val preview: List<ArticlePreview>,
        private val availableArticle: Article,
        private val availableIcon: ByteArray,
        private val availableImageId: Long
    ) : StorageSource {
        override suspend fun getArticlePreviews(): Previews {
            return Previews(preview)
        }

        override suspend fun getArticle(id: Long): Article? {
            return if (id == availableArticle.id) {
                availableArticle
            } else {
                null
            }
        }

        override suspend fun deleteArticle(id: Long): Boolean {
            error("Not implemented")
        }

        override suspend fun insertArticle(article: Article): InsertionResult? {
            error("Not implemented")
        }

        override suspend fun getArticleImage(id: Long): ByteArray? {
            return if (id == availableImageId) {
                availableIcon
            } else {
                null
            }
        }
    }
}
