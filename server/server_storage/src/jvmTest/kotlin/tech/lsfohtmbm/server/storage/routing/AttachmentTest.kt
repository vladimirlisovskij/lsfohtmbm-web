package tech.lsfohtmbm.server.storage.routing

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.api.storage.StorageApi
import tech.lsfohtmbm.entity.storage.Attachment
import tech.lsfohtmbm.server.storage.utils.buildTestRunner
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class AttachmentTest {

    @Test
    fun `get exist attachment`() = buildTestRunner {
        val testAttachmentId = 1L
        val testAttachment = byteArrayOf(1, 2, 3)
        val testAttachmentType = "image/webp"

        coEvery {
            dataBaseSource.getAttachment(testAttachmentId, true)
        } returns Attachment(testAttachment, testAttachmentType)

        GetExistAttachmentParams(testAttachmentId, testAttachment, testAttachmentType)
    }.runTest {
        val response = httpClient.get(StorageApi.ENDPOINT_ATTACHMENTS_ATTACHMENT) {
            parameter(StorageApi.PARAM_ID, parameter.testAttachmentId)
            parameter(StorageApi.PARAM_IS_FINISHED, true)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(parameter.testAttachmentType, response.headers[HttpHeaders.ContentType])
        assertContentEquals(parameter.testAttachment, response.readBytes())
        coVerify(exactly = 1) { dataBaseSource.getAttachment(parameter.testAttachmentId, true) }
    }

    @Test
    fun `get not exist attachment`() = buildTestRunner {
        val testAttachmentId = 1L
        coEvery { dataBaseSource.getAttachment(testAttachmentId, true) } returns null
        testAttachmentId
    }.runTest {
        val response = httpClient.get(StorageApi.ENDPOINT_ATTACHMENTS_ATTACHMENT) {
            parameter(StorageApi.PARAM_ID, parameter)
            parameter(StorageApi.PARAM_IS_FINISHED, true)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        coVerify(exactly = 1) { dataBaseSource.getAttachment(parameter, true) }
    }

    private class GetExistAttachmentParams(
        val testAttachmentId: Long,
        val testAttachment: ByteArray,
        val testAttachmentType: String,
    )
}
