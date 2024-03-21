package online.k0ras1k.travelagent.database.schemas

import online.k0ras1k.travelagent.data.enums.NoteStatus
import org.jetbrains.exposed.dao.id.IntIdTable

object NoteTable : IntIdTable("notes") {
    val tgId = long("tg_id")
    val adventureId = integer("adventure_id")
    val noteUrl = varchar("note_url", 200)
    val status = enumerationByName("status", 20, NoteStatus::class)
}
