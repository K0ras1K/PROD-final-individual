package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.models.AdventureCityData
import online.k0ras1k.travelagent.database.schemas.AdventureCityTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class AdventureCityPersistence(val adventureId: Int) {
    fun insertCity(adventureCityData: AdventureCityData) {
        try {
            transaction {
                AdventureCityTable.insert {
                    it[AdventureCityTable.adventureId] = adventureCityData.adventureId
                    it[AdventureCityTable.name] = adventureCityData.name
                    it[AdventureCityTable.endTime] = adventureCityData.endTime
                    it[AdventureCityTable.startTime] = adventureCityData.startTime
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun selectCities(): List<AdventureCityData> {
        return try {
            transaction {
                AdventureCityTable.selectAll().where { AdventureCityTable.adventureId.eq(adventureId) }
                    .orderBy(AdventureCityTable.startTime to SortOrder.ASC)
                    .map {
                        AdventureCityData(
                            id = it[AdventureCityTable.id].value,
                            adventureId = it[AdventureCityTable.adventureId],
                            name = it[AdventureCityTable.name],
                            startTime = it[AdventureCityTable.startTime],
                            endTime = it[AdventureCityTable.endTime],
                        )
                    }
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    fun selectCity(cityId: Int): AdventureCityData? {
        return try {
            transaction {
                AdventureCityTable.selectAll().where { AdventureCityTable.id.eq(cityId) }.single()
                    .let {
                        AdventureCityData(
                            id = cityId,
                            name = it[AdventureCityTable.name],
                            startTime = it[AdventureCityTable.startTime],
                            endTime = it[AdventureCityTable.endTime],
                            adventureId = it[AdventureCityTable.adventureId],
                        )
                    }
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun deleteCity(cityId: Int) {
        try {
            transaction {
                AdventureCityTable.deleteWhere { AdventureCityTable.id.eq(cityId) }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
