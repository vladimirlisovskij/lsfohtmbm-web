package tech.lsfohtmbm.source.database.impl

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import tech.lsfohtmbm.source.database.impl.database.Database

internal fun createDatabase(driver: SqlDriver): Database {
    return Database(driver)
}

internal fun createDriver(path: String): SqlDriver {
    return JdbcSqliteDriver(
        url = "jdbc:sqlite:$path",
        schema = Database.Schema
    )
}
