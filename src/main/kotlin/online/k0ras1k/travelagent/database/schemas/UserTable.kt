package online.k0ras1k.travelagent.database.schemas

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object UserTable: IntIdTable("users") {

    val name = varchar("name", 50)
    val tgLogin = varchar("tg_login", 50)
    val tgId = long("tg_id")

}