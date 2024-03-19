package online.k0ras1k.travelagent.utils

import online.k0ras1k.travelagent.data.models.StatusData

object Caching {
    val status_cache: MutableMap<Long, StatusData> = mutableMapOf()
}
