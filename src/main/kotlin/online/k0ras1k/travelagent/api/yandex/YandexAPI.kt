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
import online.k0ras1k.travelagent.api.yandex.data.CityResponse
import online.k0ras1k.travelagent.api.yandex.data.SegmentModel
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class YandexAPI {
    private val cityCoordinatesApiUrl = "https://geocode-maps.yandex.ru/1.x"
    private val mapsToken = dotenv()["YANDEX_MAPS_TOKEN"]

    private val stationFindApiUrl = "https://api.rasp.yandex.net/v3.0/nearest_settlement/"
    private val ticketsFindApiUrl = "https://api.rasp.yandex.net/v3.0/search/"
    private val raspToken = dotenv()["YANDEX_RASP_TOKEN"]

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
            println("starting founding code for $city")

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
}
