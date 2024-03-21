package online.k0ras1k.travelagent.controller.callback.adventure.city

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Logger
import online.k0ras1k.travelagent.api.sightsafari.SightSafariAPI
import online.k0ras1k.travelagent.api.sightsafari.data.SightArea
import online.k0ras1k.travelagent.api.yandex.YandexAPI
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class ShowSightsHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0)
            val city = persistence.selectCity(cityId)!!

            val sights = SightSafariAPI().getTravels(YandexAPI().findCityArea(city.name))

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateText(sights),
                replyMarkup = InlineKeyboardMarkup.create(listOf(KeyboardUtils.getBackButton())),
            )
        }
    }

    fun generateText(sights: List<SightArea>): String {
        if (sights == null) {
            return "Достопримечательности не найдены."
        }
        var stringBuilder = ""
        var counter = 0
        stringBuilder += "Рекомендуем посетить: \n"
        for (sight in sights) {
            counter++
            stringBuilder += "$counter. ${sight.name} - ${sight.description}\n"
        }
        Logger.logger.info(stringBuilder)
        return stringBuilder
    }
}
