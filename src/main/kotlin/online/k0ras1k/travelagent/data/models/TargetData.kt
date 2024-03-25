package online.k0ras1k.travelagent.data.models

data class TargetData(
    val name: String,
    val id: Int,
    val cityId: Int,
    val createdAt: Long,
    val date: Long,
    val time: String,
    val receipt: Int,
    val description: String,
)
