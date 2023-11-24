package tech.lsfohtmbm.source.database.impl

import kotlinx.coroutines.Dispatchers
import tech.lsfohtmbm.source.database.api.DataBaseSource
import tech.lsfohtmbm.source.database.impl.mapper.DateMapper
import tech.lsfohtmbm.source.database.impl.mapper.ParagraphMapper

fun createDataBaseSource(path: String): DataBaseSource {
    return SqlDelightDataBaseSource(
        ioDispatcher = Dispatchers.IO,
        database = createDatabase(path),
        paragraphMapper = ParagraphMapper(),
        dateMapper = DateMapper()
    )
}