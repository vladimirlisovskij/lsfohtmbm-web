package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.html.HTML
import kotlinx.html.unsafe
import tech.lsfohtmbm.entity.storage.Article
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.server.frontweb.utils.buildTestRunner
import tech.lsfohtmbm.utils.tests.ktor.mockHtml
import kotlin.test.Test
import kotlin.test.assertEquals

class ArticlePageTest {

    @Test
    fun `get article`() = buildTestRunner {
        val testHtml: HTML.(Article) -> Unit = { unsafe { +it.toString() } }
        val testArticle = Article(
            1,
            "title",
            DateWrapper(1, 2, 3),
            false,
            emptyList()
        )

        coEvery { storageSource.getArticle(testArticle.id) } returns testArticle

        modules.add {
            routing {
                configureArticlePage(storageSource, testHtml)
            }
        }

        GetArticleParameter(testHtml, testArticle)
    }.runTest {
        val response = httpClient.get("/article/${parameter.testArticle.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            mockHtml { parameter.testHtml(this, parameter.testArticle) },
            response.bodyAsText()
        )

        coVerify(exactly = 1) { storageSource.getArticle(parameter.testArticle.id) }
    }

    private class GetArticleParameter(
        val testHtml: HTML.(Article) -> Unit,
        val testArticle: Article,
    )
}
