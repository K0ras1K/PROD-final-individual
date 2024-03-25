package online.k0ras1k.travelagent.data.models

import java.util.UUID

data class PaymentData(
    val id: Int,
    val userId: Long,
    val name: String,
    val cityId: Int,
    val count: Int,
    val targetId: UUID,
)
