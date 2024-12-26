package tech.lsfohtmbm.server.adminweb.routing

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.html.HTML
import kotlinx.html.unsafe
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.server.adminweb.utils.buildTestRunner
import tech.lsfohtmbm.utils.tests.ktor.mockHtml
import kotlin.test.assertEquals

class RootEditorTest {

     @Test
    fun `get editor page`() = buildTestRunner {
        val testHtml: HTML.() -> Unit = { unsafe { +"testHtml" } }
        editor = testHtml
        testHtml
    }.runTest {
        val response = httpClient.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        val receivedBody = response.body<String>()
        assertEquals(mockHtml { parameter() }, receivedBody)
    }
}
