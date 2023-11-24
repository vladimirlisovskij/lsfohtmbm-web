package tech.lsfohtmbm.app.adminweb

import kotlinx.html.HTML
import tech.lsfohtmbm.page.admin.adminEditorPage
import tech.lsfohtmbm.server.adminweb.runServer
import tech.lsfohtmbm.source.storage.impl.createStorageSource
import tech.lsfohtmbm.utils.app.requireArgument

private const val ARG_STORAGE_HOST = "--storage"
private const val ARG_HOST = "--host"
private const val ARG_PORT = "--port"

fun main(arguments: Array<String>) {
    val storageHost = arguments.requireArgument(ARG_STORAGE_HOST)
    val host = arguments.requireArgument(ARG_HOST)
    val port = arguments.requireArgument(ARG_PORT).toInt()

    val storage = createStorageSource(storageHost)

    runServer(
        host,
        port,
        storage,
        HTML::adminEditorPage
    )
}
