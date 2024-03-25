package online.k0ras1k.travelagent.controller.callback.adventure.city.target

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.redis.StatusMachine

class AddTargetHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            StatusMachine.setStatus(
                chatId,
                StatusData(
                    status = TextStatus.TARGET_ADD_NAME,
                    headMessage = headMessage,
                    data = mutableListOf(cityId.toString()),
                ),
            )
            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = "Чем вы планируете заниматься?",
            )
        }
    }
}
