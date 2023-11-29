package tech.lsfohtmbm.source.admin.impl

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import tech.lsfohtmbm.source.admin.api.AdminSource

fun createAdminSource(baseUrl: String): AdminSource {
    return KtorAdminSource(
        createClient(createEngine()),
        baseUrl
    )
}

internal fun createClient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json()
        }
    }
}

private fun createEngine(): HttpClientEngine {
    return Js.create()
}
