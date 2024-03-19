package online.k0ras1k.travelagent.database.redis

import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Initialization
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.utils.Caching

object StatusMachine {
    fun setStatus(
        userId: Long,
        statusData: StatusData,
    ) {
        Caching.status_cache[userId] = statusData
        runBlocking {
            try {
                Initialization.redisPool.resource.set(
                    userId.toString(),
                    statusData.asString(),
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    fun getStatus(userId: Long): StatusData? {
        return if (Caching.status_cache.containsKey(userId)) {
            Caching.status_cache[userId]
        } else {
            getRedisStatus(userId)
        }
    }

    private fun getRedisStatus(userId: Long): StatusData? {
        return try {
            StatusData.valueOf(Initialization.redisPool.resource.get(userId.toString()))
        } catch (exception: Exception) {
            null
        }
    }

    fun removeStatus(userId: Long) {
        try {
            Initialization.redisPool.resource.del(
                userId.toString(),
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
