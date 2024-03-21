package online.k0ras1k.travelagent.controller.callback.adventure.notes

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import online.k0ras1k.travelagent.utils.KeyboardUtils

class AddNoteTypeHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        val chatId = callbackQuery.message?.chat?.id ?: return
        val headMessage = callbackQuery.message?.messageId ?: return
        val adventureId: Int = callbackQuery.data.split("-")[2].toInt()

        bot.editMessageText(
            chatId = ChatId.fromId(chatId),
            messageId = headMessage,
            text = "Выберите тип заметки",
            replyMarkup = KeyboardUtils.generateNoteButtons(adventureId),
        )
    }
}
