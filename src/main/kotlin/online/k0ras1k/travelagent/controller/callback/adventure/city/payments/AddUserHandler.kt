package online.k0ras1k.travelagent.controller.callback.adventure.city.payments

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.redis.StatusMachine

class AddUserHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val initiator: Long = callbackQuery.data.split("-")[2].toLong()
            val userId: Long = callbackQuery.data.split("-")[3].toLong()

            val status = StatusMachine.getStatus(initiator)
            println(status)

            if (!status!!.data.contains(userId.toString())) {
                StatusMachine.setStatus(
                    userId = chatId,
                    StatusData(
                        status = TextStatus.PAYMENT_ADD_USERS,
                        headMessage = headMessage,
                        data = (status!!.data + userId.toString()).toMutableList(),
                    ),
                )
            }
        }
    }
}
