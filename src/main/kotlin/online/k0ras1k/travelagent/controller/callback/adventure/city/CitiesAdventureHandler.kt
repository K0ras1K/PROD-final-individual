package online.k0ras1k.travelagent.controller.callback.adventure.city

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.AdventureCityData
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class CitiesAdventureHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val adventureId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(adventureId)
            val cities = persistence.selectCities()

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateText(cities),
                replyMarkup = KeyboardUtils.generateCitiesButtons(cities, adventureId),
            )
        }
    }

    private fun generateText(cities: List<AdventureCityData>): String {
        var stringBuilder: String = "Города путешествия:\n"
        var counter = 0
        for (city in cities) {
            counter += 1
            stringBuilder += "$counter. ${city.name}. Заезд: ${TimeUtils.toTimeString(
                city.startTime,
            )}. Выезд: ${TimeUtils.toTimeString(city.endTime)}\n"
        }
        return stringBuilder
    }
}
