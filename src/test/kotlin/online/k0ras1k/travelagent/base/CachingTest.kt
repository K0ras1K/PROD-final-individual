package online.k0ras1k.travelagent.base

import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.redis.StatusMachine
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.function.Executable
import kotlin.test.Test
import kotlin.test.assertEquals

class CachingTest {
    @Test
    fun testCache() {
        StatusMachine.setStatus(
            1L,
            StatusData(
                status = TextStatus.TARGET_ADD_TIME,
                headMessage = 0L,
                data = mutableListOf("123"),
            ),
        )
        val status = StatusMachine.getStatus(1)!!
        assertAll(
            "Caching",
            Executable {
                assertEquals(status.status, TextStatus.TARGET_ADD_TIME)
                assertEquals(status.data[0], "123")
            },
        )
    }

    @Test
    fun testDelete() {
        StatusMachine.setStatus(
            1L,
            StatusData(
                status = TextStatus.TARGET_ADD_TIME,
                headMessage = 0L,
                data = mutableListOf("123"),
            ),
        )
        StatusMachine.removeStatus(1)
        assertEquals(StatusMachine.getStatus(1), null)
    }
}
