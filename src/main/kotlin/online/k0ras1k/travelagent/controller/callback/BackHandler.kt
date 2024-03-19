package online.k0ras1k.travelagent.controller.callback

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.templates.MainMessage

class BackHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking

            MainMessage(bot, chatId, headMessage).toHeadMessage()
        }
    }
}
