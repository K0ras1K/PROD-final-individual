@file:Suppress("ktlint:standard:no-wildcard-imports")

package online.k0ras1k.travelagent.api.aviasales

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import online.k0ras1k.travelagent.api.aviasales.data.*

class AviaSalesAPI {
    private val apiUrl = "https://api.travelpayouts.com/v2/prices/nearest-places-matrix"
    private val airportsApiUrl = "https://suggest.aviasales.ru/v2/places.json"

    suspend fun search(
        origin: String,
        destination: String,
        departureAt: String,
    ): SearchResponse? {
        return runBlocking {
            val client =
                HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json()
                    }
                }

            val response =
                client.get(apiUrl) {
                    url {
                        parameters.append("currency", "rub")
                        parameters.append("origin", origin)
                        parameters.append("destination", destination)
                        parameters.append("show_to_affiliates", "true")
                        parameters.append("depart_date", departureAt)
                        parameters.append("token", dotenv()["AVIASALES_TOKEN"])
                    }
                    contentType(ContentType.Application.Json)
                }
            response.body<SearchResponse>()
        }
    }

    fun getCityCode(city: String): String? {
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
                client.get(airportsApiUrl) {
                    url {
                        parameters.append("local", "ru_RU")
                        parameters.append("max", "7")
                        parameters.append("term", city)
                    }
                    contentType(ContentType.Application.Json)
                }
            val airports = response.body<List<Airport>>()
            if (airports.isNotEmpty()) {
                if (airports[0].city_code != "") {
                    airports[0].city_code
                } else {
                    airports[0].code
                }
            } else {
                null
            }
        }
    }
}
