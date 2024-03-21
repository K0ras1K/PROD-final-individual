package online.k0ras1k.travelagent.api.hotellook

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import online.k0ras1k.travelagent.api.hotellook.data.Hotel

class HotelLookAPI {
    private val hotelLookApiUrl = "https://engine.hotellook.com/api/v2/cache.json"

    fun findHotel(
        city: String,
        checkIn: String,
        checkOut: String,
    ): List<Hotel> {
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
                client.get(hotelLookApiUrl) {
                    url {
                        parameters.append("location", city)
                        parameters.append("currency", "rub")
                        parameters.append("limit", "7")
                        parameters.append("checkIn", checkIn)
                        parameters.append("checkOut", checkOut)
                    }
                }
            response.body<List<Hotel>>()
        }
    }
}
