package tech.lsfohtmbm.server.storage.routing

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.DateWrapper
import tech.lsfohtmbm.entity.storage.FinishedPreview
import tech.lsfohtmbm.entity.storage.FinishedPreviews
import tech.lsfohtmbm.entity.storage.Preview
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.server.storage.utils.buildTestRunner
import kotlin.test.assertEquals

class PreviewsTests {
    @Test
    fun `get previews`() = buildTestRunner {
        val testPreviewList = listOf(
            Preview(1, "title", DateWrapper(0, 0, 0), true),
            Preview(2, "title", DateWrapper(0, 0, 0), false),
        )

        val testPreviews = Previews(testPreviewList)
        coEvery { dataBaseSource.getPreviews() } returns testPreviews
        testPreviews
    }.runTest {
        val response = httpClient.get(StorageApi.ENDPOINT_ARTICLES_PREVIEWS) {
            parameter(StorageApi.PARAM_IS_FINISHED, false)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<Previews>()
        assertEquals(parameter, receivedBody)
        coVerify(exactly = 1) { dataBaseSource.getPreviews() }
    }

    @Test
    fun `get finished previews`() = buildTestRunner {
        val testPreviewList = listOf(
            FinishedPreview(1, "title", DateWrapper(0, 0, 0)),
            FinishedPreview(2, "title", DateWrapper(0, 0, 0)),
        )

        val testPreviews = FinishedPreviews(testPreviewList)
        coEvery { dataBaseSource.getFinishedPreviews() } returns testPreviews
        testPreviews
    }.runTest {
        val response = httpClient.get(StorageApi.ENDPOINT_ARTICLES_PREVIEWS) {
            parameter(StorageApi.PARAM_IS_FINISHED, true)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<FinishedPreviews>()
        assertEquals(parameter, receivedBody)
        coVerify(exactly = 1) { dataBaseSource.getFinishedPreviews() }
    }
}
