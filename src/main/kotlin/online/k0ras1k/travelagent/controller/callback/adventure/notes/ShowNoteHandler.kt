package online.k0ras1k.travelagent.controller.callback.adventure.notes

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import online.k0ras1k.travelagent.data.enums.NoteMediaType
import online.k0ras1k.travelagent.database.persistence.NotePersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class ShowNoteHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        val chatId = callbackQuery.message?.chat?.id ?: return
        val headMessage = callbackQuery.message?.messageId ?: return
        val noteId: Int = callbackQuery.data.split("-")[2].toInt()

        val note = NotePersistence().selectNote(noteId)

        when (note!!.type) {
            NoteMediaType.TEXT -> {
                bot.sendMessage(
                    chatId = ChatId.fromId(chatId),
                    text =
                        """
                        Заметка ${note.id}:
                        
                        ${note.noteUrl}
                        """.trimIndent(),
                    replyMarkup = KeyboardUtils.generateRouteButton(),
                )
            }

            NoteMediaType.FILE -> {
                bot.sendDocument(
                    chatId = ChatId.fromId(chatId),
                    document = TelegramFile.ByFileId(note.noteUrl),
                    replyMarkup = KeyboardUtils.generateRouteButton(),
                )
            }

            NoteMediaType.PHOTO -> {
                bot.sendPhoto(
                    chatId = ChatId.fromId(chatId),
                    photo = TelegramFile.ByFileId(note.noteUrl),
                    replyMarkup = KeyboardUtils.generateRouteButton(),
                )
            }
        }
    }
}
