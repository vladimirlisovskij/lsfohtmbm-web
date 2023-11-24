package tech.lsfohtmbm.app.storage

import tech.lsfohtmbm.server.storage.runServer
import tech.lsfohtmbm.source.database.impl.createDataBaseSource
import tech.lsfohtmbm.utils.app.requireArgument


private const val ARG_DATABASE_PATH = "--database"
private const val ARG_HOST = "--host"
private const val ARG_PORT = "--port"


fun main(arguments: Array<String>) {
    val dataBasePath = arguments.requireArgument(ARG_DATABASE_PATH)
    val host = arguments.requireArgument(ARG_HOST)
    val port = arguments.requireArgument(ARG_PORT).toInt()
    val homePath = System.getProperty("user.home")
    val dataBaseSource = createDataBaseSource("$homePath/$dataBasePath")
    runServer(dataBaseSource, port, host)
}
