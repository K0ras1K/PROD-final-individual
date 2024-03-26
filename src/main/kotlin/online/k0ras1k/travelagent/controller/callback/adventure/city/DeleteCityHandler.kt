package online.k0ras1k.travelagent.controller.callback.adventure.city

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.AdventureData
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.database.persistence.AdventurePersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class DeleteCityHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0)
            val adventureId = persistence.selectCity(cityId)!!.adventureId
            persistence.deleteCity(cityId)

            val adventureData: AdventureData = AdventurePersistence().select(adventureId)!!
            val cities = AdventureCityPersistence(adventureId).selectCities()

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateAdventureText(adventureData),
                parseMode = ParseMode.HTML,
                replyMarkup = KeyboardUtils.generateFullAdventureButtons(adventureData, cities),
            )
        }
    }

    private fun generateAdventureText(adventureData: AdventureData): String {
        return """
            ${if (adventureData.name == "") "/черновик/" else adventureData.name}
            <b>Создано</b>: ${TimeUtils.toTimeString(adventureData.createdAt)}
            """.trimIndent()
    }
}
