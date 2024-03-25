package online.k0ras1k.travelagent.controller.callback.adventure.city.payments

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.redis.StatusMachine

class AddPaymentsHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            StatusMachine.setStatus(
                userId = chatId,
                StatusData(
                    status = TextStatus.PAYMENT_ADD_NAME,
                    headMessage = headMessage,
                    data = mutableListOf(cityId.toString()),
                ),
            )

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = "На что были потрачены деньги?",
            )
        }
    }
}
