package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.enums.NoteStatus
import online.k0ras1k.travelagent.data.models.NoteData
import online.k0ras1k.travelagent.database.schemas.NoteTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class NotePersistence(val tgId: Long, val adventureId: Int) {
    fun insert(noteData: NoteData) {
        try {
            transaction {
                NoteTable.insert {
                    it[NoteTable.tgId] = tgId
                    it[NoteTable.adventureId] = adventureId
                    it[NoteTable.noteUrl] = noteData.noteUrl
                    it[NoteTable.status] = noteData.status
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
                        )
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }

    fun selectNotes(): List<NoteData> {
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
                        )
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }
}
