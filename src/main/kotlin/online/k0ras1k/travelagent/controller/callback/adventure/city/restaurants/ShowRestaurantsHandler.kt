package online.k0ras1k.travelagent.controller.callback.adventure.city.restaurants

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.api.yandex.YandexAPI
import online.k0ras1k.travelagent.api.yandex.data.RestaurantData
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class ShowRestaurantsHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0).selectCity(cityId)

            val restaurants = YandexAPI().findOrganiztion("${persistence!!.name}, рестораны")

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateText(restaurants),
                replyMarkup = InlineKeyboardMarkup.create(listOf(KeyboardUtils.getBackButton())),
            )
        }
    }

    private fun generateText(restaurants: List<RestaurantData>): String {
        var stringBuilder: String = "Рестораны:\n"
        var counter = 0
        for (restaurant in restaurants) {
            counter += 1
            stringBuilder +=
                """
                $counter. ${restaurant.name}
                Адрес: ${restaurant.address}
                Телефон: ${restaurant.phone}
                """.trimIndent()
            stringBuilder += "-------------\n"
        }
        return stringBuilder
    }
}
