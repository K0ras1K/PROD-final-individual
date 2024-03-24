package online.k0ras1k.travelagent.api.geoapify

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import online.k0ras1k.travelagent.api.geoapify.data.CoordinatesData
import online.k0ras1k.travelagent.api.geoapify.data.Feature
import online.k0ras1k.travelagent.api.geoapify.data.GeocodingModel
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.util.UUID

class GeoapifyAPI {
    private val token = dotenv()["GEOAPIFY_TOKEN"]
    private val geocodingApiUrl = "https://api.geoapify.com/v1/geocode/search"
    private val routeApiIrl = "https://api.geoapify.com/v1/routing"

    fun findByText(text: String): GeocodingModel {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                explicitNulls = false
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val respond =
                client.get(geocodingApiUrl) {
                    url {
                        parameters.append("text", text)
                        parameters.append("apiKey", token)
                    }
                }
            respond.body<GeocodingModel>()
        }
    }

    fun filterList(geocodingModel: GeocodingModel): List<Feature> {
        return geocodingModel.features.sortedByDescending { it.properties.rank.popularity }
    }

    fun getRoute(dots: List<CoordinatesData>): List<CoordinatesData> {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                explicitNulls = false
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val respond =
                client.get(routeApiIrl) {
                    url {
                        parameters.append("waypoints", generateRequestString(dots))
                        parameters.append("mode", "drive")
                        parameters.append("format", "json")
                        parameters.append("apiKey", token)
                    }
                }.bodyAsText()

            try {
                val parser = JSONParser()
                val json = parser.parse(respond) as JSONObject
                val results = json["results"] as JSONArray
                val result = results[0] as JSONObject
                val geometry = ((result["geometry"] as JSONArray)[0] as JSONArray).toJSONString()
                val newDots = Json.decodeFromString<List<CoordinatesData>>(geometry)
                newDots
            } catch (exception: Exception) {
                dots
            }
        }
    }

    private fun generateRequestString(dots: List<CoordinatesData>): String {
        var stringBuilder: String = ""
        for (dot in dots) {
            stringBuilder += "${dot.lon},${dot.lat}|"
        }
        stringBuilder = stringBuilder.substring(0, stringBuilder.length - 2)
        return stringBuilder
    }

    fun generateMapRequestString(dots: List<CoordinatesData>): String {
        var stringBuilder: String = ""
        var counter = 0
        for (dot in dots) {
            counter++
            if (counter % getLinkDel(dots.size) == 0) {
                stringBuilder += "${dot.lon},${dot.lat},"
            }
        }
        stringBuilder = stringBuilder.substring(0, stringBuilder.length - 2)
        return stringBuilder
    }

    fun getMapLink(request: String): String {
        @Suppress("ktlint:standard:max-line-length")
        return "https://maps.geoapify.com/v1/staticmap?style=klokantech-basic&width=1000&height=700&geometry=polyline:$request;linewidth:3;linecolor:%2322223b;linestyle:solid;lineopacity:1&apiKey=cd0e8c4ea13e4dd7bbb5ca6679b31006"
    }

    private fun getLinkDel(count: Int): Int {
        if (count < 1000) {
            return 1
        }
        return count / (count / 100)
    }

    fun getFile(url: String): File {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                explicitNulls = false
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val uuid: UUID = UUID.randomUUID()
            val file = File("temp/maps/$uuid.png")
            client.get(url).bodyAsChannel().copyAndClose(file.writeChannel())
            println("Finished downloading..")
            file
        }
    }
}

fun main() {
    val api = GeoapifyAPI()
    api.getFile(
        api.getMapLink(
            api.generateMapRequestString(
                api.getRoute(
                    listOf(
                        CoordinatesData.getFromList(api.filterList(api.findByText("Москва"))[0].geometry.coordinates),
                        CoordinatesData.getFromList(api.filterList(api.findByText("Владивосток"))[0].geometry.coordinates),
                    ),
                ),
            ),
        ),
    )
}
