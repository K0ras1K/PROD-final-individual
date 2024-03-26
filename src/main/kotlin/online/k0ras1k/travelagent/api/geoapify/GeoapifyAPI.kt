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
import kotlin.math.ceil

class GeoapifyAPI {
    private val token = dotenv()["GEOAPIFY_TOKEN"]
    private val geocodingApiUrl = "https://api.geoapify.com/v1/geocode/search"
    private val routeApiIrl = "https://graphhopper.com/api/1/route"
    private val routeApiKey = "8c2a246f-17db-4b02-8e90-8d47ee256f97"

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
                client.get(
                    "$routeApiIrl?${generateRequestString(
                        dots,
                    )}&profile=car&points_encoded=false&locale=en&calc_points=true&key=$routeApiKey",
                ) {
                }.bodyAsText()

            try {
                val parser = JSONParser()
                val json = parser.parse(respond) as JSONObject
                val paths = json["paths"] as JSONArray
                val points = (paths[0] as JSONObject)["points"] as JSONObject
                val coordinates = points["coordinates"] as JSONArray

                var tempList: List<CoordinatesData> = listOf()

                for (coordinate in coordinates) {
                    tempList += CoordinatesData((coordinate as JSONArray)[1] as Double, (coordinate as JSONArray)[0] as Double)
                }

                println(tempList)
                tempList
            } catch (exception: Exception) {
                dots
            }
        }
    }

    private fun generateRequestString(dots: List<CoordinatesData>): String {
        var stringBuilder: String = ""
        for (dot in dots) {
            stringBuilder += "point=${dot.lon},${dot.lat}&"
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

    fun generateMarkers(dots: List<CoordinatesData>): String {
        var stringBuilder: String = ""
        var counter = 0
        for (dot in dots) {
            counter++
            @Suppress("ktlint:standard:max-line-length")
            if (counter % getLinkDel(dots.size) == 0) {
                stringBuilder += "lonlat:${dot.lat},${dot.lon};type:material;color:%23222222;size:x-large;icon:cloud;icontype:awesome;text:$counter;whitecircle:no|"
            }
        }
        stringBuilder = stringBuilder.substring(0, stringBuilder.length - 1)
        return stringBuilder
    }

    fun getMapLink(
        request: String,
        dots: List<CoordinatesData>,
    ): String {
        @Suppress("ktlint:standard:max-line-length")
        return "https://maps.geoapify.com/v1/staticmap?style=klokantech-basic&width=1000&height=700&geometry=polyline:$request;linewidth:3;linecolor:%2322223b;linestyle:solid;lineopacity:1&apiKey=cd0e8c4ea13e4dd7bbb5ca6679b31006&marker=${generateMarkers(
            dots,
        )}"
    }

    private fun getLinkDel(count: Int): Int {
        if (count < 60) {
            return 1
        }
        return ceil((count / 60.0)).toInt()
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
    val routeUuid =
        api.getFile(
            api.getMapLink(
                api.generateMapRequestString(
                    api.getRoute(
                        listOf(
                            CoordinatesData.getFromList(api.filterList(api.findByText("Нижний Новгород"))[0].geometry.coordinates),
                            CoordinatesData.getFromList(api.filterList(api.findByText("Огниково"))[0].geometry.coordinates),
//                            CoordinatesData.getFromList(api.filterList(api.findByText("Владивосток"))[0].geometry.coordinates),
//                            CoordinatesData.getFromList(api.filterList(api.findByText("Уфа"))[0].geometry.coordinates),
//                            CoordinatesData.getFromList(api.filterList(api.findByText("Кемерово"))[0].geometry.coordinates),
//                            CoordinatesData.getFromList(api.filterList(api.findByText("Минск"))[0].geometry.coordinates),
//                            CoordinatesData.getFromList(api.filterList(api.findByText("Кёльн"))[0].geometry.coordinates),
//                            CoordinatesData.getFromList(api.filterList(api.findByText("Сидней"))[0].geometry.coordinates),
                        ),
                    ),
                ),
                listOf(
                    CoordinatesData.getFromList(api.filterList(api.findByText("Нижний Новгород"))[0].geometry.coordinates),
                    CoordinatesData.getFromList(api.filterList(api.findByText("Огниково"))[0].geometry.coordinates),
//                    CoordinatesData.getFromList(api.filterList(api.findByText("Владивосток"))[0].geometry.coordinates),
//                    CoordinatesData.getFromList(api.filterList(api.findByText("Уфа"))[0].geometry.coordinates),
//                    CoordinatesData.getFromList(api.filterList(api.findByText("Кемерово"))[0].geometry.coordinates),
//                    CoordinatesData.getFromList(api.filterList(api.findByText("Минск"))[0].geometry.coordinates),
//                    CoordinatesData.getFromList(api.filterList(api.findByText("Кёльн"))[0].geometry.coordinates),
//                            CoordinatesData.getFromList(api.filterList(api.findByText("Сидней"))[0].geometry.coordinates),
                ),
            ),
        )
}
