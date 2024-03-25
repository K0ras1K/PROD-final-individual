package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.models.AdventureInviteData
import online.k0ras1k.travelagent.database.schemas.AdventureInvitesTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class AdventureInvitesPersistence {
    fun insert(adventureInviteData: AdventureInviteData) {
        try {
            transaction {
                AdventureInvitesTable.insert {
                    it[AdventureInvitesTable.adventureId] = adventureInviteData.adventureId
                    it[AdventureInvitesTable.invitedUser] = adventureInviteData.invitedUser
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun selectByUser(userId: Long): List<Int> {
        return try {
            transaction {
                AdventureInvitesTable.selectAll()
                    .where { AdventureInvitesTable.invitedUser.eq(userId) }
                    .map {
                        it[AdventureInvitesTable.adventureId]
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }

    fun selectByAdventure(adventureId: Int): List<Long> {
        return try {
            transaction {
                AdventureInvitesTable.selectAll()
                    .where { AdventureInvitesTable.adventureId.eq(adventureId) }
                    .map {
                        it[AdventureInvitesTable.invitedUser]
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }
}
