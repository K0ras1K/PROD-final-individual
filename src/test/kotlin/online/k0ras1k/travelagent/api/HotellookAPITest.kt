package online.k0ras1k.travelagent.api

import online.k0ras1k.travelagent.api.hotellook.HotelLookAPI
import online.k0ras1k.travelagent.utils.TimeUtils
import kotlin.test.Test
import kotlin.test.assertTrue

class HotellookAPITest {
    @Test
    fun testFinding() {
        val api = HotelLookAPI()
        val hotels =
            api.findHotel(
                "Москва",
                TimeUtils.toTicketString(System.currentTimeMillis()),
                TimeUtils.toTicketString(System.currentTimeMillis() + 86400000),
            )
        assertTrue(hotels.isNotEmpty())
    }
}
