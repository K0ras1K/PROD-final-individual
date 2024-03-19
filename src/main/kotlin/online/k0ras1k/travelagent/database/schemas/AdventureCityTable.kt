package online.k0ras1k.travelagent.database.schemas

import org.jetbrains.exposed.dao.id.IntIdTable

object AdventureCityTable : IntIdTable("adventure_cities") {
    val name = varchar("name", 100)
    val startTime = long("start_time")
    val endTime = long("end_time")
    val adventureId = integer("adventure_id")
}
