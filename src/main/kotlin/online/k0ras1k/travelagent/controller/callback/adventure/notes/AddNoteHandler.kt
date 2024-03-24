package online.k0ras1k.travelagent.controller.callback.adventure.notes

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import online.k0ras1k.travelagent.data.enums.NoteStatus
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.redis.StatusMachine

class AddNoteHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        val chatId = callbackQuery.message?.chat?.id ?: return
        val headMessage = callbackQuery.message?.messageId ?: return
        val status = NoteStatus.valueOf(callbackQuery.data.split("-")[3])
        val adventureId: Int = callbackQuery.data.split("-")[4].toInt()

        StatusMachine.setStatus(
            chatId,
            StatusData(
                status = TextStatus.NOTE_ADD_NAME,
                headMessage = headMessage,
                data = mutableListOf(adventureId.toString(), status.toString()),
            ),
        )
        bot.editMessageText(
            chatId = ChatId.fromId(chatId),
            messageId = headMessage,
            text = "Введите имя новой заметки",
        )
    }
}
