package online.k0ras1k.travelagent.api.yandex.data

data class RestaurantData(
    val name: String,
    val description: String,
    val address: String,
    val phone: String? = "",
)
