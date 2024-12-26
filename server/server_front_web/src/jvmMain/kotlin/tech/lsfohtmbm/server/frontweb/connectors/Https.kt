package tech.lsfohtmbm.server.frontweb.connectors

import io.ktor.server.engine.EngineSSLConnectorBuilder
import tech.lsfohtmbm.server.frontweb.HostConfig
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore

internal fun createHttpsConnector(
    config: HostConfig
): EngineSSLConnectorBuilder {
    val keyStoreFile = File(config.keyStore)

    val keyStore = KeyStore
        .getInstance("JKS")
        .apply { load(keyStoreFile, config.keyStorePassword.toCharArray()) }

    return EngineSSLConnectorBuilder(
        keyStore = keyStore,
        keyAlias = config.keyAlias,
        keyStorePassword = { config.keyStorePassword.toCharArray() },
        privateKeyPassword = { config.privateKeyPassword.toCharArray() }
    ).apply {
        port = config.sslPort
        host = config.host
    }
}

private fun KeyStore.load(file: File, password: CharArray) {
    FileInputStream(file).use { load(it, password) }
}
