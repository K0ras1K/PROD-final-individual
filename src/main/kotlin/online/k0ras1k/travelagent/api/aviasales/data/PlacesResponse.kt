package online.k0ras1k.travelagent.api.aviasales.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class Airport(
    val id: String,
    val type: String,
    val code: String,
    val name: String,
    val country_code: String,
    val country_name: String,
    val city_code: String? = "",
    val city_name: String? = "",
    val state_code: String? = "", // Изменено на String?
    val coordinates: Coordinates,
    val index_strings: List<String>,
    val weight: Int,
    val city_cases: Map<String, String>? = mapOf(),
    val country_cases: Map<String, String>? = mapOf(),
)

@Serializable
data class Coordinates(
    val lon: Double,
    val lat: Double,
)

@Serializer(forClass = Any::class)
object AnySerializer
