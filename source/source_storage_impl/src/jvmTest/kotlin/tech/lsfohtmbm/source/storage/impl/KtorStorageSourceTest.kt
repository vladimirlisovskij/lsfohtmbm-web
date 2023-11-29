package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.*
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
        var handledRequest: HttpRequestData? = null
        val source = mockSource { request ->
            handledRequest = request
            respondBadRequest()
        }

        val response = runBlocking { source.getArticlePreviews() }
        assertNotNull(handledRequest)
        assertEquals(URL_PREVIEWS, handledRequest!!.url.toStringWithoutQuery())
        assertEquals(HttpMethod.Get, handledRequest!!.method)
        assertNull(response)
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
        var handledRequest: HttpRequestData? = null
        val source = mockSource { request ->
            handledRequest = request
            respondBadRequest()
        }

        runBlocking { source.getArticle(ARTICLE_ID) }
        assertNotNull(handledRequest)
        assertEquals(URL_ARTICLE, handledRequest!!.url.toStringWithoutQuery())
        assertEquals(HttpMethod.Get, handledRequest!!.method)
        val articleId = handledRequest!!.url.parameters[StorageApi.PARAM_ID]?.toLongOrNull()
        assertEquals(ARTICLE_ID, articleId)
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
        var handledRequest: HttpRequestData? = null
        val source = mockSource { request ->
            handledRequest = request
            respondBadRequest()
        }

        runBlocking { source.deleteArticle(ARTICLE_ID) }
        assertNotNull(handledRequest)
        assertEquals(URL_DELETE, handledRequest!!.url.toStringWithoutQuery())
        assertEquals(HttpMethod.Post, handledRequest!!.method)
        assert(handledRequest!!.body.contentType!!.match(ContentType.Application.FormUrlEncoded))
        val formBody = handledRequest!!.body as FormDataContent
        assertEquals(ARTICLE_ID, formBody.formData[StorageApi.PARAM_ID]?.toLongOrNull())
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
        var handledRequest: HttpRequestData? = null
        val source = mockSource { request ->
            handledRequest = request
            respondBadRequest()
        }

        runBlocking { source.insertArticle(mockArticle) }
        assertNotNull(handledRequest)
        assertEquals(URL_INSERT, handledRequest!!.url.toStringWithoutQuery())
        assertEquals(HttpMethod.Post, handledRequest!!.method)
        assert(handledRequest!!.body.contentType!!.match(ContentType.Application.Json))
        val textBody = handledRequest!!.body as TextContent
        assertEquals(mockArticle, Json.decodeFromString<Article>(textBody.text))
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
        var handledRequest: HttpRequestData? = null
        val source = mockSource { request ->
            handledRequest = request
            respondBadRequest()
        }

        runBlocking { source.getArticleImage(IMAGE_ID) }
        assertNotNull(handledRequest)
        assertEquals(URL_IMAGE, handledRequest!!.url.toStringWithoutQuery())
        assertEquals(HttpMethod.Get, handledRequest!!.method)
        assertEquals(IMAGE_ID, handledRequest!!.url.parameters[StorageApi.PARAM_ID]?.toLongOrNull())
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
        val isImageSame = image
            .mapIndexed { index, byte -> mockImage[index] == byte  }
            .all { it }

        assert(isImageSame)
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
        return "${protocol.name}://${host}:${port}${pathSegments.joinToString("/")}"
    }
}