package tech.lsfohtmbm.server.frontweb.modules.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.html.HTML

internal fun Routing.configureRootPage(
    rootPage: HTML.() -> Unit
) {
    get("/") {
        call.respondHtml(HttpStatusCode.OK, rootPage)
    }
}
