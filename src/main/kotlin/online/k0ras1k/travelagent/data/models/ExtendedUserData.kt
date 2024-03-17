package online.k0ras1k.travelagent.data.models

import online.k0ras1k.travelagent.database.schemas.ExtendedUserTable

data class ExtendedUserData (
    val telegramId: Long,
    val userOld: Int,
    val countryCity: String,
    val bio: String
)