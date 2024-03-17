package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.database.schemas.ExtendedUserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class ExtendedUserPersistence(val tgId: Long) {

    fun insertEmpty() {
        transaction {
            ExtendedUserTable.insert {
                it[telegramId] = tgId
            }
        }
    }

    

}