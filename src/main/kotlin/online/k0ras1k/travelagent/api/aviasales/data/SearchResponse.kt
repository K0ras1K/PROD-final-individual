package online.k0ras1k.travelagent.api.aviasales.data

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val prices: List<Price>,
    val origins: List<String>,
    val destinations: List<String>,
)

@Serializable
data class Price(
    val link: String,
    val origin: String,
    val gate: String,
    val main_airline: String,
    val depart_date: String,
    val destination: String,
    val found_at: String,
    val transfers: Int,
    val distance: Int,
    val duration: Int,
    val price: Int,
    val trip_class: Int,
)
