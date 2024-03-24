package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.enums.NoteStatus
import online.k0ras1k.travelagent.data.models.NoteData
import online.k0ras1k.travelagent.database.schemas.NoteTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class NotePersistence() {
    fun insert(noteData: NoteData) {
        try {
            transaction {
                NoteTable.insert {
                    it[NoteTable.tgId] = noteData.tgId
                    it[NoteTable.adventureId] = noteData.adventureId
                    it[NoteTable.noteUrl] = noteData.noteUrl
                    it[NoteTable.status] = noteData.status
                    it[NoteTable.type] = noteData.type
                    it[NoteTable.name] = noteData.name
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun updateStatus(
        id: Int,
        status: NoteStatus,
    ) {
        try {
            transaction {
                NoteTable.update({ NoteTable.id.eq(id) }) {
                    it[NoteTable.status] = status
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun selectNote(id: Int): NoteData? {
        return try {
            transaction {
                NoteTable.selectAll().where { NoteTable.id.eq(id) }.single()
                    .let {
                        NoteData(
                            id = it[NoteTable.id].value,
                            tgId = it[NoteTable.tgId],
                            adventureId = it[NoteTable.adventureId],
                            noteUrl = it[NoteTable.noteUrl],
                            status = it[NoteTable.status],
                            type = it[NoteTable.type],
                            name = it[NoteTable.name],
                        )
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }

    fun selectNotes(
        tgId: Long,
        adventureId: Int,
    ): List<NoteData> {
        return try {
            transaction {
                NoteTable.selectAll()
                    .where { NoteTable.tgId.eq(tgId) and NoteTable.adventureId.eq(adventureId) }
                    .map {
                        NoteData(
                            id = it[NoteTable.id].value,
                            tgId = it[NoteTable.tgId],
                            adventureId = it[NoteTable.adventureId],
                            noteUrl = it[NoteTable.noteUrl],
                            status = it[NoteTable.status],
                            type = it[NoteTable.type],
                            name = it[NoteTable.name],
                        )
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }
}
