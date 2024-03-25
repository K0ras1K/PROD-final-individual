package online.k0ras1k.travelagent.controller.callback.adventure

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.AdventureCityData
import online.k0ras1k.travelagent.data.models.AdventureData
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.database.persistence.AdventureInvitesPersistence
import online.k0ras1k.travelagent.database.persistence.AdventurePersistence
import online.k0ras1k.travelagent.database.persistence.ExtendedUserPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class CreateAdventureHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking

            val extendedUserPersistence = ExtendedUserPersistence(chatId)
            if (extendedUserPersistence.select() == null) {
                bot.sendMessage(
                    chatId = ChatId.fromId(chatId),
                    text = "Вы не можете создавать путешествия, не заполнив информацию о себе",
                    replyMarkup = KeyboardUtils.generateRouteButton(),
                )
                return@runBlocking
            }

            val adventureData =
                AdventureData(
                    id = 0,
                    createdAt = System.currentTimeMillis(),
                    name = "",
                    description = "",
                    createdBy = chatId,
                )
            AdventurePersistence().insert(adventureData)
            val extendedUserData = ExtendedUserPersistence(chatId).select()!!
            val allAdventures = AdventurePersistence().selectAll(chatId)
            val firstCityData =
                AdventureCityData(
                    id = 0,
                    name = extendedUserData.countryCity,
                    startTime = 5,
                    endTime = -1,
                    adventureId = allAdventures[0].id,
                )
            AdventureCityPersistence(allAdventures[0].id).insertCity(firstCityData)

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
