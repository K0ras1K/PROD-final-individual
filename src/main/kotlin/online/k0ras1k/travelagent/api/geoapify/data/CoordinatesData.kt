package online.k0ras1k.travelagent.api.geoapify.data

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatesData(
    val lat: Double,
    val lon: Double,
) {
    companion object {
        fun getFromList(list: List<Double>): CoordinatesData {
            return CoordinatesData(
                lat = list[0],
                lon = list[1],
            )
        }
    }
}
