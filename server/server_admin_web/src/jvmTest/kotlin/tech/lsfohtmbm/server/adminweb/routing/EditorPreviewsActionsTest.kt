package tech.lsfohtmbm.server.adminweb.routing

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentLength
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.api.adminweb.AdminWebApi
import tech.lsfohtmbm.entity.storage.Previews
import tech.lsfohtmbm.server.adminweb.utils.buildTestRunner
import kotlin.test.assertEquals

class EditorPreviewsActionsTest {

    @Test
    fun `get previews with success`() = buildTestRunner {
        val previews = Previews(emptyList())
        coEvery { storageSource.getPreviews() } returns previews
        previews
    }.runTest {
        val response = httpClient.get(AdminWebApi.ENDPOINT_PREVIEWS)
        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<Previews>()
        assertEquals(parameter, receivedBody)
        coVerify(exactly = 1) { storageSource.getPreviews() }
    }

    @Test
    fun `get previews with error`() = buildTestRunner {
        val previews = Previews(emptyList())
        coEvery { storageSource.getPreviews() } returns null
        previews
    }.runTest {
        val response = httpClient.get(AdminWebApi.ENDPOINT_PREVIEWS)
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(0, response.contentLength())
        coVerify(exactly = 1) { storageSource.getPreviews() }
    }
}
