package tech.lsfohtmbm.databasesource.impl

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import tech.lsfohtmbm.databasesource.impl.database.Database

internal fun createDatabase(path: String): Database {
    return Database(JdbcSqliteDriver("jdbc:sqlite:$path"))
}
