package tech.lsfohtmbm.server.adminweb.routing

import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.Routing

internal fun Routing.configureStaticResources() {
    staticResources("/static", null)
}
