package online.k0ras1k.travelagent.api.yandex.data

import kotlinx.serialization.Serializable

@Serializable
data class CityResponse(
    val type: String,
    val title: String,
    val short_title: String,
    val popular_title: String,
    val code: String,
    val lat: Double,
    val lng: Double,
    val distance: Double,
)
