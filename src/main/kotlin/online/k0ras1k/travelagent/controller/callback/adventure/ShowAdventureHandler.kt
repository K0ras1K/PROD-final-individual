package online.k0ras1k.travelagent.controller.callback.adventure

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.database.persistence.AdventureInvitesPersistence
import online.k0ras1k.travelagent.database.persistence.AdventurePersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class ShowAdventureHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking

            val persistence = AdventurePersistence()
            val invitedAdventures = AdventureInvitesPersistence().selectByUser(chatId)

            var adventures = persistence.selectAll(chatId) + persistence.selectFromList(invitedAdventures)

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = "Мои путешествия",
                replyMarkup = KeyboardUtils.generateAdventuresKeyboard(adventures),
            )
        }
    }
}
