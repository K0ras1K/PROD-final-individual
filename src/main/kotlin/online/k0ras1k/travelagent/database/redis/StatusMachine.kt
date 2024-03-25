package online.k0ras1k.travelagent.database.redis

import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Initialization
import online.k0ras1k.travelagent.Logger
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.utils.Caching

object StatusMachine {
    fun setStatus(
        userId: Long,
        statusData: StatusData,
    ) {
        Logger.logger.info("Setting status to local cache")
        Caching.status_cache[userId] = statusData
        Logger.logger.info("Setted status to local cache")
//        runBlocking {
//            try {
//                Logger.logger.info("Setting status to redis cache")
//                Initialization.redisPool.resource.set(
//                    userId.toString(),
//                    statusData.asString(),
//                )
//                Logger.logger.info("Setted status to redis cache")
//            } catch (exception: Exception) {
//                exception.printStackTrace()
//            }
//        }
    }

    fun getStatus(userId: Long): StatusData? {
        return if (Caching.status_cache.containsKey(userId)) {
            Logger.logger.info("Returning from local cache")
            Caching.status_cache[userId]
        } else {
            Logger.logger.info("Returning from redis cache")
//            getRedisStatus(userId)
            null
        }
    }

    private fun getRedisStatus(userId: Long): StatusData? {
        return try {
            runBlocking {
                Logger.logger.info(" getting from redis cache")
                StatusData.valueOf(Initialization.redisPool.resource.get(userId.toString()))
            }
        } catch (exception: Exception) {
            null
        }
    }

    fun removeStatus(userId: Long) {
        Logger.logger.info("Removing status from local cache")
        Caching.status_cache.remove(userId)
        Logger.logger.info("Removed status from local cache")
//        try {
//            runBlocking {
//                Logger.logger.info("Removing status from redis cache")
//                Initialization.redisPool.resource.del(
//                    userId.toString(),
//                )
//                Logger.logger.info("Removed status from redis cache")
//            }
//        } catch (exception: Exception) {
//            exception.printStackTrace()
//        }
    }
}
