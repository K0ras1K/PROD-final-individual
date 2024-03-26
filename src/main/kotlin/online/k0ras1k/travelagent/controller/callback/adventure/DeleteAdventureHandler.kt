package online.k0ras1k.travelagent.controller.callback.adventure

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.database.persistence.AdventureInvitesPersistence
import online.k0ras1k.travelagent.database.persistence.AdventurePersistence
import online.k0ras1k.travelagent.templates.MainMessage

class DeleteAdventureHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val adventureId: Int = callbackQuery.data.split("-")[2].toInt()

            AdventurePersistence().delete(adventureId)
            AdventureInvitesPersistence().deleteById(adventureId)
            MainMessage(bot, chatId, headMessage).toHeadMessage()
        }
    }
}
