package online.k0ras1k.travelagent.controller.callback.adventure.edit.city

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.redis.StatusMachine

class AddCityHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val adventureId: Int = callbackQuery.data.split("-")[2].toInt()

            StatusMachine.setStatus(
                chatId,
                StatusData(
                    status = TextStatus.ADVENTURE_CITY_ADD,
                    headMessage = headMessage,
                    data = mutableListOf(adventureId.toString()),
                ),
            )
            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = "Введите название нового города",
            )
        }
    }
}
