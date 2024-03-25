package online.k0ras1k.travelagent.database.schemas

import org.jetbrains.exposed.sql.Table

object PaymentsTable : Table("payments") {
    val userId = long("user_id")
    val name = varchar("name", 100)
    val cityId = integer("cityId")
    val count = integer("count")
}
