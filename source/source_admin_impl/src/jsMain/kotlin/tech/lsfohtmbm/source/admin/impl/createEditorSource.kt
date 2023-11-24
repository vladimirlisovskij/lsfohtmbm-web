package tech.lsfohtmbm.source.admin.impl

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import tech.lsfohtmbm.source.admin.api.AdminSource

fun createAdminSource(baseUrl: String): AdminSource {
    return KtorAdminSource(
        createClient(),
        baseUrl
    )
}
private fun createClient(): HttpClient {
    return HttpClient(Js) {
        install(ContentNegotiation) {
            json()
        }
    }
}
