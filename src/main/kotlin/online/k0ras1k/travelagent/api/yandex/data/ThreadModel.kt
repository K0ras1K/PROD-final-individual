package online.k0ras1k.travelagent.api.yandex.data

import kotlinx.serialization.Serializable

@Serializable
data class SegmentModel(
    val thread: ThreadModel,
    val from: FromToModel,
    val to: FromToModel,
    val duration: Double,
    val departure: String? = "",
    val arrival: String? = "",
    val departure_platform: String? = "",
    val arrival_platform: String? = "",
    val departure_terminal: String? = "",
    val arrival_terminal: String? = "",
    val has_transfers: Boolean,
    val tickets_info: TicketInfo,
    val stops: String? = "",
    val start_date: String? = "",
)

@Serializable
data class TicketInfo(
    val et_marker: Boolean,
    val places: List<String>,
)

@Serializable
data class ThreadModel(
    val number: String? = "",
    val title: String? = "",
    val uid: String? = "",
    val short_title: String? = "",
    val express_type: String? = "",
    val transport_type: String? = "",
    val carrier: Carries,
    val vehicle: String? = "",
    val transport_subtype: TransportSubtype,
    val thread_method_link: String? = "",
)

@Serializable
data class FromToModel(
    val type: String,
    val title: String,
    val short_title: String? = "",
    val popular_title: String? = "",
    val code: String? = "",
    val station_type: String? = "",
    val station_type_name: String? = "",
    val transport_type: String? = "",
)

@Serializable
data class Carries(
    val code: Int,
    val title: String? = "",
    val codes: Codes,
    val address: String? = "",
    val url: String? = "",
    val email: String? = "",
    val contacts: String? = "",
    val phone: String? = "",
    val logo: String? = "",
    val logo_svg: String? = "",
)

@Serializable
data class Codes(
    val sirena: String? = "",
    val iata: String? = "",
    val icao: String? = "",
)

@Serializable
data class TransportSubtype(
    val title: String? = "",
    val code: String? = "",
    val color: String? = "",
)
