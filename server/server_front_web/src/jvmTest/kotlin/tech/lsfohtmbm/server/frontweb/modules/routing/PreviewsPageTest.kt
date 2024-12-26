package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.html.HTML
import kotlinx.html.unsafe
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.FinishedPreview
import tech.lsfohtmbm.entity.storage.FinishedPreviews
import tech.lsfohtmbm.server.frontweb.utils.buildTestRunner
import tech.lsfohtmbm.utils.tests.ktor.mockHtml
import kotlin.test.assertEquals

class PreviewsPageTest {

    @Test
    fun `get previews`() = buildTestRunner {
        val testHtml: HTML.(FinishedPreviews) -> Unit = { unsafe { +it.toString() } }
        val testPreviews = FinishedPreviews(listOf(FinishedPreview(1L, "1", DateWrapper(1, 2, 3))))
        coEvery { storageSource.getFinishedPreviews() } returns testPreviews

        modules.add {
            routing {
                configurePreviewsPage(storageSource, testHtml)
            }
        }

        GetPreviewsParameter(testHtml, testPreviews)
    }.runTest {
        val response = httpClient.get("/articles")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            mockHtml { parameter.html(this, parameter.previews) },
            response.bodyAsText()
        )

        coVerify(exactly = 1) { storageSource.getFinishedPreviews() }
    }

    private class GetPreviewsParameter(
        val html: HTML.(FinishedPreviews) -> Unit,
        val previews: FinishedPreviews,
    )
}