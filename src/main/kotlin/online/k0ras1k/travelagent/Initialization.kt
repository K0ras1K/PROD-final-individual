package online.k0ras1k.travelagent

import online.k0ras1k.travelagent.database.schemas.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import redis.clients.jedis.JedisPool

object Initialization {
    private val TABLES_LIST: List<Table> =
        listOf(
            UserTable,
            ExtendedUserTable,
            AdventureTable,
            AdventureCityTable,
            NoteTable,
            AdventureInvitesTable,
        )

    val redisPool =
        JedisPool(
            "redis",
            6379,
        )

    fun initialize() {
        for (table in TABLES_LIST) {
            transaction {
                SchemaUtils.create(table as Table)
            }
        }
    }
}
