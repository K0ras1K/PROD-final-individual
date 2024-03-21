package online.k0ras1k.travelagent.api.rzd.data

import kotlinx.serialization.Serializable

@Serializable
data class RidResponse(
    val result: String,
    val RID: Long,
    val timestamp: String,
)
