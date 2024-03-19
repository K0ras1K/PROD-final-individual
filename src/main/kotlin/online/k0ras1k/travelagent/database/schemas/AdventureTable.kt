package online.k0ras1k.travelagent.database.schemas

import org.jetbrains.exposed.dao.id.IntIdTable

object AdventureTable : IntIdTable("adventures") {
    val createdAt = long("created_at")
    val name = varchar("name", 100)
    val description = varchar("description", 400)
    val createdBy = long("created_by")
}
