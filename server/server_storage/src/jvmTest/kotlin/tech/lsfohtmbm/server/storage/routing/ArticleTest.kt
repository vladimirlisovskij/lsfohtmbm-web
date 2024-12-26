package tech.lsfohtmbm.server.storage.routing

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.http.parametersOf
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.FinishedArticle
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.server.storage.utils.buildTestRunner
import kotlin.test.assertEquals

class ArticleTest {
    @Test
    fun `get exist article`() = buildTestRunner {
        val testArticle = Article(1, "title", DateWrapper(0, 0, 0), true, emptyList())
        coEvery { dataBaseSource.getArticle(testArticle.id) } returns testArticle
        testArticle
    }.runTest { ->
        val response = httpClient.get(StorageApi.ENDPOINT_ARTICLES_ARTICLE) {
            parameter(StorageApi.PARAM_ID, parameter.id)
            parameter(StorageApi.PARAM_IS_FINISHED, false)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<Article>()
        assertEquals(parameter, receivedBody)
        coVerify(exactly = 1) { dataBaseSource.getArticle(parameter.id) }
    }

    @Test
    fun `get not exist article`() = buildTestRunner {
        coEvery { dataBaseSource.getArticle(any()) } returns null
    }.runTest { ->
        val response = httpClient.get(StorageApi.ENDPOINT_ARTICLES_ARTICLE) {
            parameter(StorageApi.PARAM_ID, -1)
            parameter(StorageApi.PARAM_IS_FINISHED, false)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(0, response.contentLength())
        coVerify(exactly = 1) { dataBaseSource.getArticle(any()) }
    }

    @Test
    fun `get exist finished article`() = buildTestRunner {
        val testFinishedArticle = FinishedArticle(2, "title", DateWrapper(0, 0, 0), emptyList())
        coEvery { dataBaseSource }
        testFinishedArticle
    }.runTest {
        val response = httpClient.get(StorageApi.ENDPOINT_ARTICLES_ARTICLE) {
            parameter(StorageApi.PARAM_ID, parameter.id)
            parameter(StorageApi.PARAM_IS_FINISHED, true)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<FinishedArticle>()
        assertEquals(parameter, receivedBody)
        coVerify(exactly = 1) { dataBaseSource.getFinishedArticle(parameter.id) }
    }

    @Test
    fun `get not exist finished article`() = buildTestRunner {
        coEvery { dataBaseSource.getFinishedArticle(any()) } returns null
    }.runTest {
        val response = httpClient.get(StorageApi.ENDPOINT_ARTICLES_ARTICLE) {
            parameter(StorageApi.PARAM_ID, -1)
            parameter(StorageApi.PARAM_IS_FINISHED, true)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(0, response.contentLength())
        coVerify(exactly = 1) { dataBaseSource.getFinishedArticle(any()) }
    }

    @Test
    fun `delete exist article`() = buildTestRunner {
        val articleId = 1L
        coEvery { dataBaseSource.deleteArticle(articleId) } returns true
        articleId
    }.runTest {
        val response = httpClient.delete(StorageApi.ENDPOINT_ARTICLES_ARTICLE) {
            setBody(FormDataContent(parametersOf(StorageApi.PARAM_ID, parameter.toString())))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        coVerify(exactly = 1) { dataBaseSource.deleteArticle(parameter) }
    }

    @Test
    fun `delete not exist article`() = buildTestRunner {
        coEvery { dataBaseSource.deleteArticle(any()) } returns false
    }.runTest {
        val response = httpClient.delete(StorageApi.ENDPOINT_ARTICLES_ARTICLE) {
            setBody(FormDataContent(parametersOf(StorageApi.PARAM_ID, "-1")))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(0, response.contentLength())
        coVerify(exactly = 1) { dataBaseSource.deleteArticle(any()) }
    }

    @Test
    fun `insert or update article`() = buildTestRunner {
        val testArticle = Article(1, "title", DateWrapper(0, 0, 0), true, emptyList())
        val testInsertionResult = InsertionResult(1)
        coEvery { dataBaseSource.insertArticle(testArticle) } returns testInsertionResult
        InsertOrUpdateArticleParam(testArticle, testInsertionResult)
    }.runTest {
        val response = httpClient.put(StorageApi.ENDPOINT_ARTICLES_ARTICLE) {
            setBody(parameter.testArticle)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<InsertionResult>()
        assertEquals(parameter.insertionResult, receivedBody)
        coVerify(exactly = 1) { dataBaseSource.insertArticle(parameter.testArticle) }
    }

    private class InsertOrUpdateArticleParam(
        val testArticle: Article,
        val insertionResult: InsertionResult
    )
}
