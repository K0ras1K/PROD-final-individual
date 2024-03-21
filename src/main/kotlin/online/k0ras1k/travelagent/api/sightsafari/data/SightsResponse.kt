package online.k0ras1k.travelagent.api.sightsafari.data

import kotlinx.serialization.Serializable

@Serializable
data class SightsResponse(
    val code: Int,
    val description: String,
    val body: List<SightArea>,
)

@Serializable
data class SightArea(
    val originId: String,
    val name: String? = "",
    val description: String? = "",
    val type: SightAreaType,
    val coordinates: List<List<Double>>,
    val centroid: List<Double>,
    val links: List<String>? = listOf(),
    val wikiDescription: String? = "",
    val geometryType: GeometryType,
)

enum class SightAreaType {
    TOURISM,
    WATER,
    PARK,
    PEDESTRIAN_AREA,
    POI,
    NEGATIVE,
    ADVERTISING,
    CULTURAL_OBJECT,
    MONUMENT,
}

enum class GeometryType {
    POINT,
    LINE,
    POLYGON,
}
