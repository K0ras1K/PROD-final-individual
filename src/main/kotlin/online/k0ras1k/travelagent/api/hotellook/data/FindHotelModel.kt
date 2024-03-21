package online.k0ras1k.travelagent.api.hotellook.data

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val country: String,
    val geo: Geo,
    val state: String? = "",
    val name: String,
)

@Serializable
data class Hotel(
    val location: Location,
    val priceAvg: Double,
    val pricePercentile: Map<String, Double>,
    val hotelName: String,
    val stars: Int,
    val locationId: Int,
    val hotelId: Int,
    val priceFrom: Double,
)

@Serializable
data class Geo(
    val lat: String,
    val lon: String,
)
