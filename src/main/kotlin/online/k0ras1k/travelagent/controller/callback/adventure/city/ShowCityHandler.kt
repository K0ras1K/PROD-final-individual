package online.k0ras1k.travelagent.controller.callback.adventure.city

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.AdventureCityData
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.database.persistence.TargetPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class ShowCityHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0)
            val city = persistence.selectCity(cityId)!!

            val targets = TargetPersistence().selectAll(cityId)

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateText(city),
                replyMarkup = KeyboardUtils.generateCityButton(city, targets),
            )
        }
    }

    private fun generateText(city: AdventureCityData): String {
        return """
            Город ${city.name} в поездке с ID ${city.adventureId}
            Время прибытия: ${TimeUtils.toTimeString(city.startTime)}
            Время отправления: ${TimeUtils.toTimeString(city.endTime)}
            """.trimIndent()
    }
}
