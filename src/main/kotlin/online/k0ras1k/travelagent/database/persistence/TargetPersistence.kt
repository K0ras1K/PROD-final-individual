package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.models.TargetData
import online.k0ras1k.travelagent.database.schemas.TargetTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TargetPersistence() {
    fun insert(targetData: TargetData) {
        try {
            transaction {
                TargetTable.insert {
                    it[TargetTable.name] = targetData.name
                    it[TargetTable.cityId] = targetData.cityId
                    it[TargetTable.createdAt] = targetData.createdAt
                    it[TargetTable.date] = targetData.date
                    it[TargetTable.time] = targetData.time
                    it[TargetTable.receipt] = targetData.receipt
                    it[TargetTable.description] = targetData.description
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun selectById(targetId: Int): TargetData? {
        return try {
            transaction {
                TargetTable.selectAll()
                    .where { TargetTable.id.eq(targetId) }
                    .single()
                    .let {
                        TargetData(
                            name = it[TargetTable.name],
                            id = it[TargetTable.id].value,
                            cityId = it[TargetTable.cityId],
                            createdAt = it[TargetTable.createdAt],
                            date = it[TargetTable.date],
                            time = it[TargetTable.time],
                            receipt = it[TargetTable.receipt],
                            description = it[TargetTable.description],
                        )
                    }
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun selectAll(cityId: Int): List<TargetData> {
        return try {
            transaction {
                TargetTable.selectAll()
                    .where { TargetTable.cityId.eq(cityId) }
                    .map {
                        TargetData(
                            name = it[TargetTable.name],
                            id = it[TargetTable.id].value,
                            cityId = it[TargetTable.cityId],
                            createdAt = it[TargetTable.createdAt],
                            date = it[TargetTable.date],
                            time = it[TargetTable.time],
                            receipt = it[TargetTable.receipt],
                            description = it[TargetTable.description],
                        )
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }
}
