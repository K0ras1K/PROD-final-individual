package online.k0ras1k.travelagent.database.redis

import online.k0ras1k.travelagent.Initialization
import online.k0ras1k.travelagent.data.enums.TextStatus

object StatusMachine {
    fun setStatus(
        userId: Long,
        status: TextStatus,
        headMessageId: Long,
    ) {
        try {
            Initialization.redisPool.resource.set(
                userId.toString(),
                "$status:$headMessageId",
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun getStatus(userId: Long): TextStatus? {
        return try {
            TextStatus.valueOf(
                Initialization.redisPool.resource.get(
                    userId.toString(),
                ).split(":")[0],
            )
        } catch (exception: Exception) {
            null
        }
    }

    fun getHeadMessage(userId: Long): Long? {
        return try {
            Initialization.redisPool.resource.get(
                userId.toString(),
            ).split(":")[1].toLong()
        } catch (exception: Exception) {
            null
        }
    }

    fun removeStatus(userId: Long): String {
        try {
            Initialization.redisPool.resource.del(
                userId.toString(),
            )
            return "deleted"
//            Initialization.redisPool.resource
        } catch (exception: Exception) {
            exception.printStackTrace()
            return exception.message.toString()
        }
//        try {
//            Initialization.redisPool.resource.set(
//                userId.toString(),
//                ""
//            )
//        }
//        catch (exception: Exception) {
//            exception.printStackTrace()
//        }
    }
}
