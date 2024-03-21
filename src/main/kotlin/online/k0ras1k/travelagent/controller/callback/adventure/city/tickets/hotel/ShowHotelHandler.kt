package online.k0ras1k.travelagent.controller.callback.adventure.city.tickets.hotel

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Logger
import online.k0ras1k.travelagent.api.hotellook.HotelLookAPI
import online.k0ras1k.travelagent.api.hotellook.data.Hotel
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class ShowHotelHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0)
            val city = persistence.selectCity(cityId)!!

            val hotels =
                HotelLookAPI().findHotel(
                    city.name,
                    TimeUtils.toTicketString(city.startTime),
                    TimeUtils.toTicketString(city.endTime),
                )

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text =
                    generateText(
                        hotels,
                        TimeUtils.toTicketString(city.startTime),
                        TimeUtils.toTicketString(city.endTime),
                    ),
                replyMarkup = KeyboardUtils.generateHotelButtons(hotels, cityId),
            )
        }
    }

    fun generateText(
        tickets: List<Hotel>,
        checkIn: String,
        checkOut: String,
    ): String {
        if (tickets == null) {
            return "Отели не найдены."
        }
        var stringBuilder = ""
        stringBuilder += "Заезд: $checkIn\n"
        stringBuilder += "Выезд: $checkOut\n"
        stringBuilder += "\n"
        stringBuilder += "Найденные отели: \n"
        for (hotel in tickets) {
            stringBuilder += "${hotel.hotelName}.\n"
            stringBuilder += "Звезд: ${hotel.stars}\n"
            stringBuilder += "Цены от: ${hotel.priceFrom}\n"
            stringBuilder += "Местонахождение: ${hotel.location.name}.\n"
            stringBuilder += "-----------------------\n"
        }
        Logger.logger.info(stringBuilder)
        return stringBuilder
    }
}
