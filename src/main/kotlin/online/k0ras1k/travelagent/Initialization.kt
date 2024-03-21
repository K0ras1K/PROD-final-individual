package online.k0ras1k.travelagent

import online.k0ras1k.travelagent.database.schemas.AdventureCityTable
import online.k0ras1k.travelagent.database.schemas.AdventureTable
import online.k0ras1k.travelagent.database.schemas.ExtendedUserTable
import online.k0ras1k.travelagent.database.schemas.UserTable
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
        )

    val redisPool =
        JedisPool(
            "redis",
            6379,
        )

    suspend fun initialize() {
        for (table in TABLES_LIST) {
            transaction {
                SchemaUtils.create(table as Table)
            }
        }
    }
}
