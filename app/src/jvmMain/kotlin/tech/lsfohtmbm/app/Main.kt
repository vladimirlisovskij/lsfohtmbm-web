package tech.lsfohtmbm.app

import kotlinx.html.HTML
import tech.lsfohtmbm.articlelistpage.articleListPage
import tech.lsfohtmbm.articlerenderer.renderArticle
import tech.lsfohtmbm.databasesource.impl.createDataBaseSource
import tech.lsfohtmbm.mainpage.mainPage
import tech.lsfohtmbm.server.HostConfig
import tech.lsfohtmbm.server.PageConfig
import tech.lsfohtmbm.server.runServer

private const val ARG_DATABASE_PATH = "--database"
private const val ARG_HOST = "--host"
private const val ARG_PORT = "--port"
private const val ARG_SSL_PORT = "--ssl_port"
private const val ARG_KEYSTORE = "--keystore"
private const val ARG_KEYSTORE_PASSWORD = "--keystore_password"
private const val ARG_KEY_ALIAS = "--key_alias"
private const val ARG_KEY_PASSWORD = "--key_password"


fun main(arguments: Array<String>) {
    val dataBasePath = arguments.requireArgument(ARG_DATABASE_PATH)
    val host = arguments.requireArgument(ARG_HOST)
    val port = arguments.requireArgument(ARG_PORT)
    val sslPort = arguments.requireArgument(ARG_SSL_PORT)
    val keyStore = arguments.requireArgument(ARG_KEYSTORE)
    val keyStorePassword = arguments.requireArgument(ARG_KEYSTORE_PASSWORD)
    val keyAlias = arguments.requireArgument(ARG_KEY_ALIAS)
    val keyPassword = arguments.requireArgument(ARG_KEY_PASSWORD)

    val pageConfig = PageConfig(
        HTML::mainPage,
        HTML::articleListPage,
        HTML::renderArticle
    )

    val homePath = System.getProperty("user.home")

    val hostConfig = HostConfig(
        host = host,
        defaultPort = port.toInt(),
        sslPort = sslPort.toInt(),
        keyStore = "$homePath/$keyStore",
        keyStorePassword = keyStorePassword,
        keyAlias = keyAlias,
        privateKeyPassword = keyPassword
    )

    runServer(
        createDataBaseSource("$homePath/$dataBasePath"),
        pageConfig,
        hostConfig
    )
}

private fun Array<String>.requireArgument(argumentName: String): String {
    return runCatching {
        val (_, value) = first { it.startsWith(argumentName) }.split("=")
        value
    }.getOrNull() ?: throw Exception("No argument passed for $argumentName")
}
