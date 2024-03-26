package online.k0ras1k.travelagent.api

import online.k0ras1k.travelagent.api.yandex.YandexAPI
import online.k0ras1k.travelagent.utils.TimeUtils
import kotlin.test.Test
import kotlin.test.assertTrue

class YandexAPITest {
    @Test
    fun testYandexRestaurants() {
        val api = YandexAPI()
        val restaurants = api.findOrganiztion("Москва, рестораны")
        assertTrue(restaurants.isNotEmpty())
    }

    @Test
    fun testYandexGeocoder() {
        val api = YandexAPI()
        val stationCode = api.findStationCode("Москва")
        assertTrue(stationCode != "")
    }

    @Test
    fun testYandexWeather() {
        val api = YandexAPI()
        val weatherData = api.getWeatherByCity("Москва", TimeUtils.toTicketString(System.currentTimeMillis()))
        assertTrue(weatherData != null)
    }
}
