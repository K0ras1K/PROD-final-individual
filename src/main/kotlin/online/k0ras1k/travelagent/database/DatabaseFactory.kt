package online.k0ras1k.travelagent.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

/**
 * Object for create the fastest HikariDatabasePool
 *
 * @author Roman Kalmykov (K0ras1K)
 */

object DatabaseFactory {

    /**
     * Initialize hikari data pool
     *
     * @param url Database connect url
     * @param driver Database driver
     * @param login Database user login
     * @param pw Database user password
     */
    fun createHikariDataSource(
        url: String,
        driver: String,
        login: String,
        pw: String
    ) = HikariDataSource(
        HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            maximumPoolSize = 10
            isAutoCommit = true
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            username = login
            password = pw
            validate()
        },
    )
}