package tech.lsfohtmbm.server.blogweb

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import kotlinx.html.HTML
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.entity.article.Article
import tech.lsfohtmbm.entity.article.ArticlePreview
import tech.lsfohtmbm.entity.article.DateWrapper
import tech.lsfohtmbm.source.database.api.DataBaseSource

private const val HOST = "192.168.0.1"
private const val DEFAULT_PORT = 8080
private const val SSL_PORT = 8443
private const val HTTP_BASE_URL = "http://$HOST:$DEFAULT_PORT"
private const val HTTPS_BASE_URL = "https://$HOST:$SSL_PORT"

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
        listOf()
    )

    private val availableImage = byteArrayOf(1, 2, 3)

    private val mockDataSource = MockDataBaseSource(
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
        val image = client.get("$HTTP_BASE_URL/image/$AVAILABLE_IMAGE_ID")
        assertEquals(HttpStatusCode.OK, image.status)
        val imageBody = image.readBytes()
        assertEquals(availableImage.size, imageBody.size)
        assert(availableImage.mapIndexed { index, byte -> imageBody[index] == byte }.all { it })
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
        +"mainPage"
    }

    private fun HTML.mockErrorPage(error: Int) {
        +"errorPage $error"
    }

    private fun HTML.mockArticleListPage(preview: List<ArticlePreview>) {
        preview.forEach {
            +it.toString()
        }
    }

    private fun HTML.mockArticleRenderer(article: Article) {
        +article.toString()
    }

    /**
     * copy-paste from ktor implementation
     */
    private fun mockHtml(block: HTML.() -> Unit) = buildString {
        append("<!DOCTYPE html>\n")
        appendHTML().html(block = block)
    }

    private class MockDataBaseSource(
        private val preview: List<ArticlePreview>,
        private val availableArticle: Article,
        private val availableIcon: ByteArray,
        private val availableImageId: Long
    ) : DataBaseSource {
        override suspend fun getArticlePreviews(): List<ArticlePreview> {
            return preview
        }

        override suspend fun getArticle(id: Long): Article? {
            return if (id == availableArticle.id) {
                availableArticle
            } else {
                null
            }
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