package online.k0ras1k.travelagent.api

import online.k0ras1k.travelagent.api.geoapify.GeoapifyAPI
import kotlin.test.Test
import kotlin.test.assertTrue

class GeoapifyAPITest {
    @Test
    fun testGeocoder() {
        val api = GeoapifyAPI()
        val geocodingModel = api.findByText("Москва")
        assertTrue(geocodingModel.features.isNotEmpty())
    }
}
