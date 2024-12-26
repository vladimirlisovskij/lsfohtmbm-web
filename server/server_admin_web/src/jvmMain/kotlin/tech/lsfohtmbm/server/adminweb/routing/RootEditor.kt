package tech.lsfohtmbm.server.adminweb.routing

import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.html.HTML

internal fun Routing.configureRootEditorRouting(
    editor: HTML.() -> Unit
) {
    get("/") {
        call.respondHtml { editor() }
    }
}
