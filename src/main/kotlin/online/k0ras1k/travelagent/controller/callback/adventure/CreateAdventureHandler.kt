package online.k0ras1k.travelagent.controller.callback.adventure

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.AdventureCityData
import online.k0ras1k.travelagent.data.models.AdventureData
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.database.persistence.AdventurePersistence
import online.k0ras1k.travelagent.database.persistence.ExtendedUserPersistence

class CreateAdventureHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking

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
        }
    }
}
