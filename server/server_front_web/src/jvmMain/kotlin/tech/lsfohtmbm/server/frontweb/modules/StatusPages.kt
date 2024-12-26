package tech.lsfohtmbm.server.frontweb.modules

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.html.respondHtml
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.uri
import tech.lsfohtmbm.server.frontweb.PageConfig

internal fun Application.configureStatusPages(
    pageConfig: PageConfig
) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondHtml {
                pageConfig.errorPage.invoke(this, HttpStatusCode.InternalServerError.value)
            }
        }

        status(
            HttpStatusCode.NotFound,
            HttpStatusCode.BadRequest,
            HttpStatusCode.MethodNotAllowed,
            HttpStatusCode.InternalServerError,
            HttpStatusCode.ServiceUnavailable,
        ) { call, status ->
            val uri = call.request.uri
            if (
                !uri.startsWith("/attachment") &&
                !uri.startsWith("/static")
            ) {
                call.respondHtml { pageConfig.errorPage.invoke(this, status.value) }
            }
        }
    }
}
