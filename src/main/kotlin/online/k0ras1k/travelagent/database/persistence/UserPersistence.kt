package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.models.UserData
import online.k0ras1k.travelagent.database.schemas.UserTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
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
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun select(): UserData? {
        return try {
            transaction {
                val respond = UserTable.selectAll().where { UserTable.tgId.eq(userData.tgId) }
                var users: MutableList<UserData> = mutableListOf()
                respond.forEach {
                    users +=
                        UserData(
                            tgId = userData.tgId,
                            tgLogin = it[UserTable.tgLogin],
                            name = it[UserTable.name],
                            id = it[UserTable.id].value,
                        )
                }
                users[0]
            }
        } catch (exception: Exception) {
            null
        }
    }
}
