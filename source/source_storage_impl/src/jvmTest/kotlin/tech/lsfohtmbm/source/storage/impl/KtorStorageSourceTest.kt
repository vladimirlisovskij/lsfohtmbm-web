package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.ArticlePreview
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.utils.tests.ktor.assertContentTypes
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private const val BASE_URL = "http://testUrl.com:1234"
private const val URL_PREVIEWS = "$BASE_URL/${StorageApi.ENDPOINT_PREVIEWS}"
private const val URL_ARTICLE = "$BASE_URL/${StorageApi.ENDPOINT_ARTICLE}"
private const val URL_DELETE = "$BASE_URL/${StorageApi.ENDPOINT_DELETE}"
private const val URL_INSERT = "$BASE_URL/${StorageApi.ENDPOINT_INSERT}"
private const val URL_IMAGE = "$BASE_URL/${StorageApi.ENDPOINT_IMAGE}"

private const val ARTICLE_ID = 1L
private const val IMAGE_ID = 2L

private const val CONTENT_JSON = "application/json"
private const val CONTENT_WEBP = "image/webp"

class KtorStorageSourceTest {

    private val mockArticle = Article(
        ARTICLE_ID,
        "",
        DateWrapper(0, 0, 0,),
        emptyList()
    )

    private val mockImage = byteArrayOf(1, 2, 3)

    @Test
    fun testArticlePreviewsParams() {
        var lastRequest: HttpRequestData? = null
        val source = mockSource { request ->
            lastRequest = request
            respondBadRequest()
        }

        runBlocking { source.getArticlePreviews() }
        val handledRequest = lastRequest
        assertNotNull(handledRequest)
        assertAll(
            "request structure",
            { assertEquals(URL_PREVIEWS, handledRequest.url.toStringWithoutQuery()) },
            { assertEquals(HttpMethod.Get, handledRequest.method) }
        )
    }

    @Test
    fun testArticlesPreviewsSuccess() {
        val previews = Previews(
            listOf(
                ArticlePreview(-1, "", DateWrapper(0, 0, 0))
            )
        )

        val source = mockSource {
            respond(
                content = Json.encodeToString(previews),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, CONTENT_JSON)
            )
        }

        val response = runBlocking { source.getArticlePreviews() }
        assertNotNull(response)
        assertEquals(previews.previews.size, response.previews.size)
        assertEquals(previews.previews.first(), response.previews.first())
    }

    @Test
    fun testArticlesPreviewsError() {
        val source = mockSource { respondBadRequest() }
        val response = runBlocking { source.getArticlePreviews() }
        assertNull(response)
    }

    @Test
    fun testArticleParams() {
        var lastRequest: HttpRequestData? = null
        val source = mockSource { request ->
            lastRequest = request
            respondBadRequest()
        }

        runBlocking { source.getArticle(ARTICLE_ID) }
        val handledRequest = lastRequest
        assertNotNull(handledRequest)
        assertAll(
            "request structure",
            { assertEquals(URL_ARTICLE, handledRequest.url.toStringWithoutQuery()) },
            { assertEquals(HttpMethod.Get, handledRequest.method) },
            {
                val articleId = handledRequest.url.parameters[StorageApi.PARAM_ID]?.toLongOrNull()
                assertEquals(ARTICLE_ID, articleId)
            }
        )
    }

    @Test
    fun testArticleSuccess() {
        val source = mockSource {
            respond(
                content = Json.encodeToString(mockArticle),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, CONTENT_JSON)
            )
        }

        val response = runBlocking { source.getArticle(ARTICLE_ID) }
        assertNotNull(response)
        assertEquals(ARTICLE_ID, response.id)
        assertEquals(mockArticle, response)
    }

    @Test
    fun testArticleError() {
        val source = mockSource { respondBadRequest() }
        val response = runBlocking { source.getArticle(ARTICLE_ID) }
        assertNull(response)
    }

    @Test
    fun testDeleteArticleParams() {
        var lastRequest: HttpRequestData? = null
        val source = mockSource { request ->
            lastRequest = request
            respondBadRequest()
        }

        runBlocking { source.deleteArticle(ARTICLE_ID) }
        val handledRequest = lastRequest
        assertNotNull(handledRequest)
        assertAll(
            "request structure",
            { assertEquals(URL_DELETE, handledRequest.url.toStringWithoutQuery()) },
            { assertEquals(HttpMethod.Post, handledRequest.method) },
            {
                val contentType = handledRequest.body.contentType!!
                assertContentTypes(ContentType.Application.FormUrlEncoded, contentType)
                val formBody = handledRequest.body as FormDataContent
                assertEquals(ARTICLE_ID, formBody.formData[StorageApi.PARAM_ID]?.toLongOrNull())
            }
        )
    }

    @Test
    fun testDeleteArticleSuccess() {
        val source = mockSource { respondOk() }
        val isSuccess = runBlocking { source.deleteArticle(ARTICLE_ID) }
        assert(isSuccess)
    }

    @Test
    fun testDeleteArticleError() {
        val source = mockSource { respondBadRequest() }
        val isSuccess = runBlocking { source.deleteArticle(ARTICLE_ID) }
        assert(!isSuccess)
    }

    @Test
    fun testInsertArticleParams() {
        var lastRequest: HttpRequestData? = null
        val source = mockSource { request ->
            lastRequest = request
            respondBadRequest()
        }

        runBlocking { source.insertArticle(mockArticle) }
        val handledRequest = lastRequest
        assertNotNull(handledRequest)
        assertAll(
            "request structure",
            { assertEquals(URL_INSERT, handledRequest.url.toStringWithoutQuery()) },
            { assertEquals(HttpMethod.Post, handledRequest.method) },
            {
                val contentType = handledRequest.body.contentType!!
                assertContentTypes(ContentType.Application.Json, contentType)
                val textBody = handledRequest.body as TextContent
                assertEquals(mockArticle, Json.decodeFromString<Article>(textBody.text))
            }
        )
    }

    @Test
    fun testInsertArticleSuccess() {
        val insertionResult = InsertionResult(ARTICLE_ID)
        val source = mockSource {
            respond(
                content = Json.encodeToString(insertionResult),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, CONTENT_JSON)
            )
        }

        val result = runBlocking { source.insertArticle(mockArticle) }
        assertNotNull(result)
        assertEquals(ARTICLE_ID, result.newArticleId)
    }

    @Test
    fun testInsertArticleError() {
        val source = mockSource {
            respondBadRequest()
        }

        val result = runBlocking { source.insertArticle(mockArticle) }
        assertNull(result)
    }

    @Test
    fun testArticleImageParams() {
        var lastRequest: HttpRequestData? = null
        val source = mockSource { request ->
            lastRequest = request
            respondBadRequest()
        }

        runBlocking { source.getArticleImage(IMAGE_ID) }
        val handledRequest = lastRequest
        assertNotNull(handledRequest)
        assertAll(
            "request structure",
            { assertEquals(URL_IMAGE, handledRequest.url.toStringWithoutQuery()) },
            { assertEquals(HttpMethod.Get, handledRequest.method) },
            { assertEquals(IMAGE_ID, handledRequest.url.parameters[StorageApi.PARAM_ID]?.toLongOrNull()) }
        )
    }

    @Test
    fun testArticleImageSuccess() {
        val source = mockSource {
            respond(
                content = mockImage,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, CONTENT_WEBP)
            )
        }

        val image = runBlocking { source.getArticleImage(IMAGE_ID) }
        assertNotNull(image)
        assertContentEquals(mockImage, image)
    }

    @Test
    fun testArticleImageError() {
        val source = mockSource { respondBadRequest() }
        val image = runBlocking { source.getArticleImage(IMAGE_ID) }
        assertNull(image)
    }

    private fun mockSource(
        block: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ): KtorStorageSource {
        return KtorStorageSource(
            createClient(MockEngine(block)),
            BASE_URL
        )
    }

    private fun Url.toStringWithoutQuery(): String {
        return "${protocol.name}://$host:${port}${pathSegments.joinToString("/")}"
    }
}
