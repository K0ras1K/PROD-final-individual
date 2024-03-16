package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.models.UserData
import online.k0ras1k.travelagent.database.schemas.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class UserPersistence(val userData: UserData) {

    fun insert() {
        try {
            transaction {
                UserTable.insert {
                    it[name] = userData.name
                    it[tgId] = userData.tgId
                    it[tgLogin] = userData.tgLogin
                }
            }
        }
        catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

}