package online.k0ras1k.travelagent.database.schemas

import org.jetbrains.exposed.dao.id.IntIdTable

object TargetTable : IntIdTable("target") {
    val name = varchar("name", 100)
    val cityId = integer("city_id")
    val createdAt = long("created_at")
    val date = long("date")
    val time = varchar("time", 20)
    val receipt = integer("receipt")
    val description = varchar("description", 200)
}
