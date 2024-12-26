package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import kotlinx.html.HTML
import kotlinx.html.unsafe
import tech.lsfohtmbm.server.frontweb.utils.buildTestRunner
import tech.lsfohtmbm.utils.tests.ktor.mockHtml
import kotlin.test.Test
import kotlin.test.assertEquals

class RootPageTest {

    @Test
    fun `get root page`() = buildTestRunner {
        val testHtml: HTML.() -> Unit = { unsafe { +"testHtml" } }
        modules.add {
            routing {
                configureRootPage(testHtml)
            }
        }

        testHtml
    }.runTest {
        val response = httpClient.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mockHtml(parameter), response.bodyAsText())
    }
}
