package tech.lsfohtmbm.source.database.impl

import kotlinx.coroutines.Dispatchers
import tech.lsfohtmbm.source.database.api.DataBaseSource
import tech.lsfohtmbm.source.database.impl.mapper.BooleanMapper
import tech.lsfohtmbm.source.database.impl.mapper.DateMapper
import tech.lsfohtmbm.source.database.impl.mapper.ParagraphMapper

fun createSqlDelightDataBaseSource(path: String): DataBaseSource {
    return SqlDelightDataBaseSource(
        Dispatchers.IO,
        createDatabase(createFileDriver(path)),
        ParagraphMapper(),
        DateMapper(),
        BooleanMapper()
    )
}
