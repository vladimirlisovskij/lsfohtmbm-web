package tech.lsfohtmbm.server.adminweb.routing

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.api.adminweb.AdminWebApi
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.InsertionResult
import tech.lsfohtmbm.server.adminweb.utils.buildTestRunner
import kotlin.test.assertEquals

class EditorArticleActionsTest {

    @Test
    fun `get exist article`() = buildTestRunner {
        val article = Article(1, "title", DateWrapper(0, 0, 0), true, emptyList())
        coEvery { storageSource.getArticle(article.id) } returns article
        article
    }.runTest {
        val response = httpClient.get(AdminWebApi.ENDPOINT_ARTICLE) {
            parameter(AdminWebApi.PARAM_ID, parameter.id)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<Article>()
        assertEquals(parameter, receivedBody)
        coVerify(exactly = 1) { storageSource.getArticle(parameter.id) }
    }

    @Test
    fun `delete exist article`() = buildTestRunner {
        val articleId = 1L
        coEvery { storageSource.deleteArticle(articleId) } returns true
        articleId
    }.runTest {
        val response = httpClient.delete(AdminWebApi.ENDPOINT_ARTICLE) {
            parameter(AdminWebApi.PARAM_ID, parameter)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(0, response.contentLength())
        coVerify(exactly = 1) { storageSource.deleteArticle(parameter) }
    }

    @Test
    fun `put article with success`() = buildTestRunner {
        val article = Article(1, "title", DateWrapper(0, 0, 0), true, emptyList())
        val result = InsertionResult(article.id)
        coEvery { storageSource.insertArticle(article) } returns result
        ArticleInsertionArgument(article, result)
    }.runTest {
        val response = httpClient.put(AdminWebApi.ENDPOINT_ARTICLE) {
            setBody(parameter.article)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<InsertionResult>()
        assertEquals(parameter.insertionResult, receivedBody)
        coVerify(exactly = 1) { storageSource.insertArticle(parameter.article) }
    }

    private class ArticleInsertionArgument(
        val article: Article,
        val insertionResult: InsertionResult?
    )
}
