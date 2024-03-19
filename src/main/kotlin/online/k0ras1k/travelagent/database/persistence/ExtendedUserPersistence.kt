package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.models.ExtendedUserData
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.schemas.ExtendedUserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExtendedUserPersistence(val tgId: Long) {
    fun select(): ExtendedUserData? {
        return try {
            transaction {
                val respond =
                    ExtendedUserTable.selectAll()
                        .where { ExtendedUserTable.telegramId.eq(tgId) }
                        .map {
                            ExtendedUserData(
                                telegramId = tgId,
                                userOld = it[ExtendedUserTable.userOld],
                                countryCity = it[ExtendedUserTable.countryCity],
                                bio = it[ExtendedUserTable.bio],
                            )
                        }
                respond[0]
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun insert(statusData: StatusData) {
        transaction {
            ExtendedUserTable.insert {
                it[ExtendedUserTable.telegramId] = tgId
                it[ExtendedUserTable.userOld] = statusData.data[0].toInt()
                it[ExtendedUserTable.bio] = statusData.data[1]
                it[ExtendedUserTable.countryCity] = statusData.data[2]
            }
        }
    }

    fun update(statusData: StatusData) {
        transaction {
            ExtendedUserTable.update({ ExtendedUserTable.telegramId.eq(tgId) }) {
                it[ExtendedUserTable.telegramId] = tgId
                it[ExtendedUserTable.userOld] = statusData.data[0].toInt()
                it[ExtendedUserTable.bio] = statusData.data[1]
                it[ExtendedUserTable.countryCity] = statusData.data[2]
            }
        }
    }
}
