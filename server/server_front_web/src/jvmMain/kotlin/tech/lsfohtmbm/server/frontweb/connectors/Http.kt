package tech.lsfohtmbm.server.frontweb.connectors

import io.ktor.server.engine.EngineConnectorBuilder
import tech.lsfohtmbm.server.frontweb.HostConfig

internal fun createHttpConnector(
    config: HostConfig
): EngineConnectorBuilder {
    return EngineConnectorBuilder().apply {
        port = config.defaultPort
        host = config.host
    }
}
