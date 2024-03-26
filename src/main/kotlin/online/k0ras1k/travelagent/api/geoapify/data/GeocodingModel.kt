package online.k0ras1k.travelagent.api.geoapify.data

import kotlinx.serialization.Serializable

@Serializable
data class GeocodingModel(
    val type: String,
    val features: List<Feature>,
    val query: Query,
)

@Serializable
data class Feature(
    val type: String,
    val properties: Properties,
    val geometry: Geometry,
    val bbox: List<Double>? = listOf(),
)

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<Double>,
)

@Serializable
data class Properties(
    val datasource: Datasource,
    val name: String? = "",
    val old_name: String? = "",
    val country: String,
    val country_code: String,
    val housenumber: String? = "",
    val region: String? = "",
    val state: String? = "",
    val county: String? = "",
    val city: String? = "",
    val postcode: String = "",
    val municipality: String? = "",
    val district: String = "",
    val neighbourhood: String = "",
    val hamlet: String? = "",
    val village: String? = "",
    val lon: Double,
    val lat: Double,
    val result_type: String? = "",
    val formatted: String? = "",
    val address_line1: String? = "",
    val address_line2: String? = "",
    val category: String? = "",
    val timezone: TimeZone,
    val plus_code: String? = "",
    val plus_code_short: String? = "",
    val rank: Rank,
    val place_id: String? = "",
)

@Serializable
data class Rank(
    val importance: Double? = 0.0,
    val popularity: Double? = 0.0,
    val confidence: Double? = 0.0,
    val confidence_city_level: Double? = 0.0,
    val match_type: String,
)

@Serializable
data class TimeZone(
    val name: String,
    val offset_STD: String,
    val offset_STD_seconds: Int,
    val offset_DST: String,
    val offset_DST_seconds: Int,
    val abbreviation_STD: String? = "",
    val abbreviation_DST: String = "",
)

@Serializable
data class Datasource(
    val sourcename: String? = "",
    val attribution: String? = "",
    val license: String? = "",
    val url: String? = "",
)

@Serializable
data class Query(
    val text: String,
    val parsed: Parsed,
)

@Serializable
data class Parsed(
    val city: String? = "",
    val expected_type: String,
)
