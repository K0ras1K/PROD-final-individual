package online.k0ras1k.travelagent.controller.callback.adventure.notes

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import online.k0ras1k.travelagent.data.enums.NoteStatus
import online.k0ras1k.travelagent.data.models.NoteData
import online.k0ras1k.travelagent.database.persistence.NotePersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class NotesMenuHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        val chatId = callbackQuery.message?.chat?.id ?: return
        val headMessage = callbackQuery.message?.messageId ?: return
        val adventureId: Int = callbackQuery.data.split("-")[2].toInt()

        val notes =
            parseNotes(
                chatId,
                NotePersistence().selectNotes(
                    chatId,
                    adventureId,
                ),
            )

        bot.editMessageText(
            chatId = ChatId.fromId(chatId),
            messageId = headMessage,
            text = "Менеджер заметок",
            replyMarkup = KeyboardUtils.generateNotesManagerButtons(adventureId, notes),
        )
    }

    fun parseNotes(
        userId: Long,
        notes: List<NoteData>,
    ): List<NoteData> {
        val tmp: MutableList<NoteData> = mutableListOf()
        for (note in notes) {
            if (note.status == NoteStatus.PUBLIC || note.tgId == userId) {
                tmp += note
            }
        }
        return tmp.toList()
    }
}
