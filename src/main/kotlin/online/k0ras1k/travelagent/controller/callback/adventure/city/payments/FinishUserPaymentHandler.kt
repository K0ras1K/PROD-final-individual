package online.k0ras1k.travelagent.controller.callback.adventure.city.payments

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.PaymentData
import online.k0ras1k.travelagent.database.persistence.PaymentPersistence
import online.k0ras1k.travelagent.database.redis.StatusMachine
import online.k0ras1k.travelagent.templates.MainMessage
import java.util.*

class FinishUserPaymentHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val initiator: Long = callbackQuery.data.split("-")[2].toLong()

            val status = StatusMachine.getStatus(initiator)
            println(status)

            val users = status!!.data.subList(3, status.data.size)
            val uuid = UUID.randomUUID()
            println(users)
            for (user in users) {
                val persistence = PaymentPersistence()
                persistence.insert(
                    PaymentData(
                        id = 0,
                        userId = user.toLong(),
                        name = status.data[1],
                        cityId = status.data[0].toInt(),
                        count = status.data[2].toInt() / users.size,
                        targetId = uuid,
                    ),
                )
            }

            MainMessage(bot, chatId, headMessage).toHeadMessage()
        }
    }
}
