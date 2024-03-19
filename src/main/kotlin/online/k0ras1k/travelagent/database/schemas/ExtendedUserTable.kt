package online.k0ras1k.travelagent.database.schemas

import org.jetbrains.exposed.sql.Table

object ExtendedUserTable : Table("extended_user") {
    val telegramId = long("tg_id")
    val userOld = integer("user_old")
    val countryCity = varchar("country_city", 100)
    val bio = varchar("bio", 300)

    override val primaryKey = PrimaryKey(telegramId)
}
