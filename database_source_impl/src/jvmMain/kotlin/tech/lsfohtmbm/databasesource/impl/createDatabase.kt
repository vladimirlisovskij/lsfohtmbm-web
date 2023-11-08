package tech.lsfohtmbm.databasesource.impl

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import tech.lsfohtmbm.databasesource.impl.database.Database


private const val DATABASE_PATH = "articles.db"

internal fun createDatabase(): Database {
    return Database(JdbcSqliteDriver("jdbc:sqlite:$DATABASE_PATH"))
}
