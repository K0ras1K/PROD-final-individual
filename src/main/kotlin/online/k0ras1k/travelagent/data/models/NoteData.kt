package online.k0ras1k.travelagent.data.models

import online.k0ras1k.travelagent.data.enums.NoteMediaType
import online.k0ras1k.travelagent.data.enums.NoteStatus

data class NoteData(
    val id: Int,
    val name: String,
    val tgId: Long,
    val adventureId: Int,
    val noteUrl: String,
    val type: NoteMediaType,
    val status: NoteStatus,
)
