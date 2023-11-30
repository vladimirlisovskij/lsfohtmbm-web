package tech.lsfohtmbm.app.frontweb

import kotlinx.html.HTML
import tech.lsfohtmbm.page.frontarticlelist.articleListPage
import tech.lsfohtmbm.page.frontarticlerenderer.renderArticle
import tech.lsfohtmbm.page.fronterror.errorPage
import tech.lsfohtmbm.page.frontmain.mainPage
import tech.lsfohtmbm.server.frontweb.HostConfig
import tech.lsfohtmbm.server.frontweb.PageConfig
import tech.lsfohtmbm.server.frontweb.runServer
import tech.lsfohtmbm.source.storage.impl.createStorageSource
import tech.lsfohtmbm.utils.app.requireArgument

private const val ARG_STORAGE_HOST = "--storage"
private const val ARG_HOST = "--host"
private const val ARG_PORT = "--port"
private const val ARG_SSL_PORT = "--ssl_port"
private const val ARG_KEYSTORE = "--keystore"
private const val ARG_KEYSTORE_PASSWORD = "--keystore_password"
private const val ARG_KEY_ALIAS = "--key_alias"
private const val ARG_KEY_PASSWORD = "--key_password"

fun main(arguments: Array<String>) {
    val storageHost = arguments.requireArgument(ARG_STORAGE_HOST)
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
        HTML::renderArticle,
        HTML::errorPage
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

    val storage = createStorageSource(storageHost)

    runServer(
        storage,
        pageConfig,
        hostConfig
    )
}
