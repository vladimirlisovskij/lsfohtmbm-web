package tech.lsfohtmbm.source.admin.impl

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
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
