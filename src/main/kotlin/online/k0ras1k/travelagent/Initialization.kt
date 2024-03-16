package online.k0ras1k.travelagent

import online.k0ras1k.travelagent.database.schemas.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object Initialization {

    val TABLES_LIST: List<Table> = listOf(
        UserTable
    )

    fun initialize() {
        for (table in TABLES_LIST) {
            transaction {
                SchemaUtils.create(table as Table)
            }
        }
    }

}