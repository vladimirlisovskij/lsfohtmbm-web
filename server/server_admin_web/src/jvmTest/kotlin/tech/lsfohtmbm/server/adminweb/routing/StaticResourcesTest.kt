package tech.lsfohtmbm.server.adminweb.routing

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Test
import tech.lsfohtmbm.server.adminweb.utils.buildTestRunner
import kotlin.test.assertEquals

class StaticResourcesTest {

    @Test
    fun `get static resource`() = buildTestRunner {
    }.runTest {
        val testResource = httpClient.get("static/test.resource")
        assertEquals(HttpStatusCode.OK, testResource.status)
        assertEquals("test", testResource.bodyAsText())
    }

    @Test
    fun `get unknown static resource`() = buildTestRunner {
    }.runTest {
        val testResource = httpClient.get("static/unknown.resource")
        assertEquals(HttpStatusCode.NotFound, testResource.status)
    }
}
