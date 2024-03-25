package online.k0ras1k.travelagent.controller.callback.adventure.city.payments

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.database.persistence.PaymentPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class ShowPaymentsMenuHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = PaymentPersistence()
            val payments = persistence.parseUnique(persistence.selectAll(cityId))

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = "Траты:",
                replyMarkup = KeyboardUtils.generatePaymentsButton(payments, cityId),
            )
        }
    }
}
