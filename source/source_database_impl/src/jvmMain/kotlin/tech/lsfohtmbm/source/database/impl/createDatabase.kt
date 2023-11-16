package tech.lsfohtmbm.source.database.impl

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import tech.lsfohtmbm.source.database.impl.database.Database

internal fun createDatabase(path: String): Database {
    return Database(JdbcSqliteDriver("jdbc:sqlite:$path"))
}
