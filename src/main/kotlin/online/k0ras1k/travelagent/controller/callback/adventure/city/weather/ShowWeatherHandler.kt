package online.k0ras1k.travelagent.controller.callback.adventure.city.weather

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.api.yandex.YandexAPI
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class ShowWeatherHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0)
            val city = persistence.selectCity(cityId)!!

            val weatherData = YandexAPI().getWeatherByCity(city.name, TimeUtils.toTicketString(city.startTime))!!

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text =
                    """
                    Прогноз погоды в городе ${city.name} на '${TimeUtils.toTicketString(city.startTime)}'
                    
                    Утро: ${weatherData.morning} ℃
                    День: ${weatherData.day} ℃
                    Вечер: ${weatherData.evening} ℃
                    Ночь: ${weatherData.night} ℃
                    """.trimIndent(),
                replyMarkup = InlineKeyboardMarkup.create(listOf(KeyboardUtils.getBackButton())),
            )
        }
    }
}
