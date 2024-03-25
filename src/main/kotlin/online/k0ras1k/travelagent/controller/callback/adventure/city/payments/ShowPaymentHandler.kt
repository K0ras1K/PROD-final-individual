package online.k0ras1k.travelagent.controller.callback.adventure.city.payments

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.PaymentRespondModel
import online.k0ras1k.travelagent.database.persistence.PaymentPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.UsersUtils
import java.util.*

class ShowPaymentHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val paymentId = UUID.fromString(callbackQuery.data.split("|")[2])

            val persistence = PaymentPersistence().select(paymentId)
            println(persistence)

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateText(persistence),
                replyMarkup = InlineKeyboardMarkup.create(listOf(KeyboardUtils.getBackButton())),
            )
        }
    }

    fun generateText(data: List<PaymentRespondModel>): String {
        var stringBuilder: String = ""
        var summ = 0
        for (payment in data) {
            summ += payment.count
            stringBuilder += "${UsersUtils.getUsernameById(payment.userId)} - $${payment.count}\n"
        }
        stringBuilder += "\n"
        stringBuilder += "Всего - $$summ"
        return stringBuilder
    }
}
