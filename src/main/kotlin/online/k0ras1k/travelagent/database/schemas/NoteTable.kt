package online.k0ras1k.travelagent.database.schemas

import online.k0ras1k.travelagent.data.enums.NoteMediaType
import online.k0ras1k.travelagent.data.enums.NoteStatus
import org.jetbrains.exposed.dao.id.IntIdTable

object NoteTable : IntIdTable("notes") {
    val tgId = long("tg_id")
    val adventureId = integer("adventure_id")
    val name = varchar("name", 100)
    val noteUrl = varchar("note_url", 200)
    val type = enumerationByName("type", 20, NoteMediaType::class)
    val status = enumerationByName("status", 20, NoteStatus::class)
}
