package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
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
