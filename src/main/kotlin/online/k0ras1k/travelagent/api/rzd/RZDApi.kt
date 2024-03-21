@file:Suppress("ktlint:standard:no-wildcard-imports")

package online.k0ras1k.travelagent.api.rzd

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import online.k0ras1k.travelagent.api.rzd.data.RidResponse
import online.k0ras1k.travelagent.api.rzd.data.SuggesterResponse

@Deprecated("Use YandexAPI instead")
class RZDApi {
    private val apiFindStationUrl = "https://pass.rzd.ru/suggester"
    private val timeTableApiUrl = "https://pass.rzd.ru/timetable/public/"

    fun getStationSuggest(city: String): Int? {
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

            val response =
                client.get(apiFindStationUrl) {
                    url {
                        parameters.append("stationNamePart", city.uppercase())
                        parameters.append("lang", "ru")
                        parameters.append("compactMode", "y")
                    }
                    contentType(ContentType.Application.Json)
                }
//            println(response.bodyAsText())
            val suggests = response.body<List<SuggesterResponse>>()
            var cc: Int? = null
            for (suggest in suggests) {
                if (suggest.ss!! == true) {
                    cc = suggest.c
                }
            }
            cc
        }
    }

    /**
     * Get trains table
     *
     * @param origin Origin station ID
     * @param destination Destination station ID
     * @param time Date of travel
     *
     * @author Roman Kalmykov
     */
    fun getTrains(
        origin: Int,
        destination: Int,
        time: String,
    ) {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(HttpCookies) {
                        storage =
                            ConstantCookiesStorage(
                                Cookie(
                                    name = "session-cookie",
                                    "17be9008aa6613305b324f5f80267f931f4896d5e2247aee9ade002a5f699bfa36dffaaa8cc5d03f6c16e43f63c0fbe3",
                                    domain = "pass.rzd.ru",
                                ),
                            )
                    }
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

            val ridResponse =
                client.get(timeTableApiUrl) {
                    url {
                        parameters.append("layer_id", "5827")
                        parameters.append("dir", "0")
                        parameters.append("code0", origin.toString())
                        parameters.append("code1", destination.toString())
                        parameters.append("tfl", "3")
                        parameters.append("checkSeats", "1")
                        parameters.append("dt0", time)
                        parameters.append("md", "0")
                    }
                    contentType(ContentType.Application.Json)
                }
            val ridJsonString = ridResponse.bodyAsText().trim().split('\n').last()
            println("parsed response - $ridJsonString")
            val rid = Json.decodeFromString<RidResponse>(ridJsonString).RID
            println("rid - $rid")

//            val fetchResponse =
//                client.get("${timeTableApiUrl}ru") {
//                    url {
//                        parameters.append("layer_id", "5827")
//                        parameters.append("rid", rid.toString())
//                    }
//                }
//            println(fetchResponse.bodyAsText())
        }
    }
}
