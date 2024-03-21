package online.k0ras1k.travelagent.api.rzd.data

import kotlinx.serialization.Serializable

@Serializable
data class SuggesterResponse(
    val n: String,
    val c: Int,
    val S: Int,
    val L: Int,
    val ss: Boolean? = false,
)
