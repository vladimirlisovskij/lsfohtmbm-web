package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import tech.lsfohtmbm.server.frontweb.utils.buildTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals

class StaticResourcesTest {

    @Test
    fun `get static resource`() = buildTestRunner {
        modules.add {
            routing {
                configureStaticResources()
            }
        }
    }.runTest {
        val response = httpClient.get("/static/test.resource")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("test", response.bodyAsText())
    }
}