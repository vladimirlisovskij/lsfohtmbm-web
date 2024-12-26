package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.routing.routing
import io.mockk.coEvery
import io.mockk.coVerify
import tech.lsfohtmbm.entity.storage.Attachment
import tech.lsfohtmbm.server.frontweb.utils.buildTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals

class AttachmentsTest {

    @Test
    fun `get exist attachment`() = buildTestRunner {
        val testAttachment = Attachment(
            data = byteArrayOf(1, 2, 3),
            type = "testType/testSubtype"
        )

        val testAttachmentId = 1L

        coEvery { storageSource.getAttachment(testAttachmentId, true) } returns testAttachment

        modules.add {
            routing {
                configureAttachments(storageSource)
            }
        }

        GetExistAttachmentArgument(testAttachmentId, testAttachment)
    }.runTest {
        val response = httpClient.get("/attachment/${parameter.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.parse(parameter.attachment.type), response.contentType())
        coVerify(exactly = 1) { storageSource.getAttachment(parameter.id, true) }
    }

    private class GetExistAttachmentArgument(
        val id: Long,
        val attachment: Attachment,
    )
}