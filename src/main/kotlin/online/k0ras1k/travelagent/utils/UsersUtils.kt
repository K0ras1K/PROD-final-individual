package online.k0ras1k.travelagent.utils

import online.k0ras1k.travelagent.data.models.UserData
import online.k0ras1k.travelagent.database.persistence.UserPersistence

object UsersUtils {
    fun getUsernameById(id: Long): String {
        val persistence = UserPersistence(UserData(0, "", "", id))
        return persistence.select()!!.tgLogin
    }
}
