package online.k0ras1k.travelagent.base

import online.k0ras1k.travelagent.utils.TimeUtils
import kotlin.test.Test
import kotlin.test.assertTrue

class TimeTest {
    @Test
    fun testTime() {
        assertTrue(TimeUtils.toMillis("20.02.2006 22:00:00") != -1L)
    }
}
