package online.k0ras1k.travelagent.api.yandex.data

data class WeatherForecast(
    val date: String,
    val morning: Double,
    val day: Double,
    val evening: Double,
    val night: Double,
)
