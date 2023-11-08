package tech.lsfohtmbm.databasesource.impl

import kotlinx.coroutines.Dispatchers
import tech.lsfohtmbm.databasesource.api.DataBaseSource
import tech.lsfohtmbm.databasesource.impl.mapper.DateMapper
import tech.lsfohtmbm.databasesource.impl.mapper.ParagraphMapper

fun createDataBaseSource(path: String): DataBaseSource {
    return DataBaseSourceImpl(
        ioDispatcher = Dispatchers.IO,
        database = createDatabase(path),
        paragraphMapper = ParagraphMapper(),
        dateMapper = DateMapper()
    )
}