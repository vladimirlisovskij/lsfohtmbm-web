package tech.lsfohtmbm.articlerepository.impl.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import tech.lsfohtmbm.articlerepository.impl.database.generated.ArticlesDatabase

internal const val DATABASE_PATH = "./articles.db"

internal fun createDatabase(): ArticlesDatabase {
    return ArticlesDatabase(
        JdbcSqliteDriver("jdbc:sqlite:$DATABASE_PATH")
    )
}
