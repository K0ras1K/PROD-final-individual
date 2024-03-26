package online.k0ras1k.travelagent.api

import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.api.aviasales.AviaSalesAPI
import online.k0ras1k.travelagent.utils.TimeUtils
import kotlin.test.Test
import kotlin.test.assertTrue

class AviasalesAPITest {
    @Test
    fun testCityCode() {
        val api = AviaSalesAPI()
        val cityCode = api.getCityCode("Москва")
        assertTrue(cityCode != null)
    }

    @Test
    fun testSearch() {
        val api = AviaSalesAPI()
        runBlocking {
            val searchResponse = api.search("GOJ", "MOW", TimeUtils.toTicketString(System.currentTimeMillis()))
        }
    }
}
