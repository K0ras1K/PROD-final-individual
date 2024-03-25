package online.k0ras1k.travelagent.api.sightsafari

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import online.k0ras1k.travelagent.api.sightsafari.data.SightArea
import online.k0ras1k.travelagent.api.sightsafari.data.SightAreaType
import online.k0ras1k.travelagent.api.sightsafari.data.SightsResponse
import online.k0ras1k.travelagent.api.yandex.data.AreaData
import kotlin.math.min

class SightSafariAPI {
    private val sightsApiUrl = "https://sightsafari.city/api/v1/geography/sights"

    fun getSights(areaData: AreaData): SightsResponse? {
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
                client.get(sightsApiUrl) {
                    url {
                        parameters.append("minLat", areaData.minLat)
                        parameters.append("minLon", areaData.minLon)
                        parameters.append("maxLat", areaData.maxLat)
                        parameters.append("maxLon", areaData.maxLon)
                    }
                }
            println(response.bodyAsText())
            try {
                response.body<SightsResponse>()
            } catch (exception: Exception) {
                null
            }
        }
    }

    fun getTravels(areaData: AreaData): List<SightArea> {
        val sights = getSights(areaData)
        val tempSights =
            sights!!.body.filter {
                it.type == SightAreaType.TOURISM || it.type == SightAreaType.CULTURAL_OBJECT || it.type == SightAreaType.MONUMENT
            }
        return tempSights.subList(0, min(5, tempSights.size))
    }
}
