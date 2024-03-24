package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.models.AdventureData
import online.k0ras1k.travelagent.database.schemas.AdventureTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class AdventurePersistence {
    fun insert(adventureData: AdventureData) {
        transaction {
            AdventureTable.insert {
                it[AdventureTable.name] = adventureData.name
                it[AdventureTable.createdAt] = System.currentTimeMillis()
                it[AdventureTable.description] = adventureData.description
                it[AdventureTable.createdBy] = adventureData.createdBy
            }
        }
    }

    fun select(id: Int): AdventureData? {
        return try {
            transaction {
                val respond = AdventureTable.selectAll().where { AdventureTable.id.eq(id) }.single()
                AdventureData(
                    id = id,
                    createdAt = respond[AdventureTable.createdAt],
                    name = respond[AdventureTable.name],
                    description = respond[AdventureTable.description],
                    createdBy = respond[AdventureTable.createdBy],
                )
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun selectAll(userId: Long): List<AdventureData> {
        return try {
            transaction {
                AdventureTable.selectAll()
                    .where { AdventureTable.createdBy.eq(userId) }
                    .orderBy(AdventureTable.createdAt to SortOrder.DESC)
                    .map {
                        AdventureData(
                            id = it[AdventureTable.id].value,
                            createdAt = it[AdventureTable.createdAt],
                            name = it[AdventureTable.name],
                            description = it[AdventureTable.description],
                            createdBy = userId,
                        )
                    }
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun selectFromList(adventures: List<Int>): List<AdventureData> {
        return try {
            transaction {
                AdventureTable.selectAll()
                    .where { AdventureTable.id.inList(adventures) }
                    .orderBy(AdventureTable.createdAt to SortOrder.DESC)
                    .map {
                        AdventureData(
                            id = it[AdventureTable.id].value,
                            createdAt = it[AdventureTable.createdAt],
                            name = it[AdventureTable.name],
                            description = it[AdventureTable.description],
                            createdBy = it[AdventureTable.createdBy],
                        )
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }

    fun updateName(
        id: Int,
        name: String,
    ) {
        try {
            transaction {
                AdventureTable.update({ AdventureTable.id.eq(id) }) {
                    it[AdventureTable.name] = name
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun updateDescription(
        id: Int,
        description: String,
    ) {
        try {
            transaction {
                AdventureTable.update({ AdventureTable.id.eq(id) }) {
                    it[AdventureTable.description] = description
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
