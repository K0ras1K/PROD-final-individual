@file:Suppress("ktlint:standard:no-wildcard-imports")

package online.k0ras1k.travelagent.api.yandex

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import online.k0ras1k.travelagent.api.yandex.data.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class YandexAPI {
    private val cityCoordinatesApiUrl = "https://geocode-maps.yandex.ru/1.x"
    private val mapsToken = dotenv()["YANDEX_MAPS_TOKEN"]

    private val stationFindApiUrl = "https://api.rasp.yandex.net/v3.0/nearest_settlement/"
    private val ticketsFindApiUrl = "https://api.rasp.yandex.net/v3.0/search/"
    private val raspToken = dotenv()["YANDEX_RASP_TOKEN"]

    private val organizationsApiUrl = "https://search-maps.yandex.ru/v1/"
    private val organizationToken = dotenv()["YANDEX_ORGANIZATIONS_TOKEN"]

    fun findCityArea(city: String): AreaData {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val cityResponse =
                client.get(cityCoordinatesApiUrl) {
                    url {
                        parameters.append("apikey", mapsToken)
                        parameters.append("geocode", city)
                        parameters.append("lang", "ru_RU")
                        parameters.append("format", "json")
                    }
                }.bodyAsText()
            println(cityResponse)

            val parser = JSONParser()
            val json = parser.parse(cityResponse) as JSONObject
            val response = json["response"] as JSONObject
            val geoObjectLocation = response["GeoObjectCollection"] as JSONObject
            val metaDataProperty = geoObjectLocation["metaDataProperty"] as JSONObject
            val geocoderResponseMetaData = metaDataProperty["GeocoderResponseMetaData"] as JSONObject
            val boundedBy = geocoderResponseMetaData["boundedBy"] as JSONObject
            val envelope = boundedBy["Envelope"] as JSONObject
            AreaData(
                minLat = (envelope["lowerCorner"] as String).split(" ")[0],
                minLon = (envelope["lowerCorner"] as String).split(" ")[1],
                maxLat = (envelope["upperCorner"] as String).split(" ")[0],
                maxLon = (envelope["upperCorner"] as String).split(" ")[1],
            )
        }
    }

    fun findStationCode(city: String): String {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val cityResponse =
                client.get(cityCoordinatesApiUrl) {
                    url {
                        parameters.append("apikey", mapsToken)
                        parameters.append("geocode", city)
                        parameters.append("lang", "ru_RU")
                        parameters.append("format", "json")
                    }
                }.bodyAsText()
            println(cityResponse)

            val parser = JSONParser()
            val json = parser.parse(cityResponse) as JSONObject
            val response = json["response"] as JSONObject
            val geoObjectLocation = response["GeoObjectCollection"] as JSONObject
            val featureMember = geoObjectLocation["featureMember"] as JSONArray
            val nullObject = featureMember[0] as JSONObject
            val geoObject = nullObject["GeoObject"] as JSONObject
            val point = geoObject["Point"] as JSONObject
            // CITY COORDINATES
            val pos = point["pos"] as String
            println("${pos.split(" ")[0]} | ${pos.split(" ")[1]}")

            val stationResponse =
                client.get(stationFindApiUrl) {
                    url {
                        parameters.append("apikey", raspToken)
                        parameters.append("lat", pos.split(" ")[1])
                        parameters.append("lng", pos.split(" ")[0])
                    }
                }
            println(stationResponse.body<CityResponse>())
            stationResponse.body<CityResponse>().code
        }
    }

    fun getTickets(
        origin: String,
        destination: String,
        time: String,
    ): List<SegmentModel> {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val response =
                client.get(ticketsFindApiUrl) {
                    url {
                        parameters.append("apikey", raspToken)
                        parameters.append("from", origin)
                        parameters.append("to", destination)
                        parameters.append("lang", "ru_RU")
                        parameters.append("date", time)
                        parameters.append("transport_types", "train")
                        parameters.append("limit", "5")
                    }
                }.bodyAsText()

            println(response)

            val parser = JSONParser()
            val json = parser.parse(response) as JSONObject
            val segments = json["segments"] as JSONArray

            Json.decodeFromString<List<SegmentModel>>(segments.toJSONString())
        }
    }

    fun getWeatherByCity(
        city: String,
        date: String,
    ): WeatherForecast? {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val cityResponse =
                client.get(cityCoordinatesApiUrl) {
                    url {
                        parameters.append("apikey", mapsToken)
                        parameters.append("geocode", city)
                        parameters.append("lang", "ru_RU")
                        parameters.append("format", "json")
                    }
                }.bodyAsText()

            val parser = JSONParser()
            val json = parser.parse(cityResponse) as JSONObject
            val response = json["response"] as JSONObject
            val geoObjectLocation = response["GeoObjectCollection"] as JSONObject
            val featureMember = geoObjectLocation["featureMember"] as JSONArray
            val nullObject = featureMember[0] as JSONObject
            val geoObject = nullObject["GeoObject"] as JSONObject
            val point = geoObject["Point"] as JSONObject
            // CITY COORDINATES
            val pos = point["pos"] as String
            println("${pos.split(" ")[0]} | ${pos.split(" ")[1]}")

            val weatherResponse =
                client.get("https://api.weather.yandex.ru/v2/forecast") {
                    url {
                        parameters.append("lat", pos.split(" ")[1])
                        parameters.append("lon", pos.split(" ")[0])
                        parameters.append("date", date.toString())
                    }
                    header("X-Yandex-API-Key", "ba728fbd-f733-45da-ac0a-3c26d1e4ebe0")
                }.bodyAsText()

            println(weatherResponse)

            val weatherJson = JSONParser().parse(weatherResponse) as JSONObject
            val forecast = weatherJson["forecasts"] as JSONArray
            val days = (forecast[0] as JSONObject)["parts"] as JSONObject

            if (days.size > 0) {
                val morning = (days["morning"] as JSONObject)["temp_avg"] as Long
                val dayInfo = (days["day"] as JSONObject)["temp_avg"] as Long
                val evening = (days["evening"] as JSONObject)["temp_avg"] as Long
                val night = (days["night"] as JSONObject)["temp_avg"] as Long

                return@runBlocking WeatherForecast(date, morning.toDouble(), dayInfo.toDouble(), evening.toDouble(), night.toDouble())
            }

            null
        }
    }

    fun findOrganiztion(text: String): List<RestaurantData> {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val response =
                client.get(organizationsApiUrl) {
                    url {
                        parameters.append("apikey", organizationToken)
                        parameters.append("lang", "ru_RU")
                        parameters.append("type", "biz")
                        parameters.append("text", text)
                    }
                }.bodyAsText()
            println(response)

            val restaurants: MutableList<RestaurantData> = mutableListOf()

            val parser = JSONParser()
            val json = parser.parse(response) as JSONObject
            val features = json["features"] as JSONArray
            for (feature in features) {
                val properties = (feature as JSONObject)["properties"] as JSONObject
                val companyMetaData = properties["CompanyMetaData"] as JSONObject
                val phones = companyMetaData["Phones"] as JSONArray
                val phone = phones[0] as JSONObject
                restaurants +=
                    RestaurantData(
                        name = companyMetaData["name"] as String,
                        description = properties["description"] as String,
                        address = companyMetaData["address"] as String,
                        phone = phone["formatted"] as String,
                    )
            }

            println(restaurants)
            restaurants
        }
    }
}
