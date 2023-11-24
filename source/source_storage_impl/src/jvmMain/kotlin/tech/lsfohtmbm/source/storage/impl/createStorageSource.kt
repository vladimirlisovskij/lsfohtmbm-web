package tech.lsfohtmbm.source.storage.impl

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import tech.lsfohtmbm.source.storage.api.StorageSource

fun createStorageSource(baseUrl: String): StorageSource {
    return KtorStorageSource(createClient(), baseUrl)
}

private fun createClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
}