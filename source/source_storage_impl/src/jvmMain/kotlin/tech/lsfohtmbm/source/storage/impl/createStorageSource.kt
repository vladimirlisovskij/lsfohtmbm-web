package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import tech.lsfohtmbm.source.storage.api.StorageSource

fun createStorageSource(baseUrl: String): StorageSource {
    return KtorStorageSource(
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
    return CIO.create()
}