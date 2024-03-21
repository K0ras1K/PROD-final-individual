package online.k0ras1k.travelagent.data.models

import online.k0ras1k.travelagent.data.enums.NoteStatus

data class NoteData(
    val id: Int,
    val tgId: Long,
    val adventureId: Int,
    val noteUrl: String,
    val status: NoteStatus,
)
