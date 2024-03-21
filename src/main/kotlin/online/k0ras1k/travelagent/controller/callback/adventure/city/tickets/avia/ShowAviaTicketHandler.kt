package online.k0ras1k.travelagent.controller.callback.adventure.city.tickets.avia

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Logger
import online.k0ras1k.travelagent.api.aviasales.AviaSalesAPI
import online.k0ras1k.travelagent.api.aviasales.data.SearchResponse
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class ShowAviaTicketHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0)
            val city = persistence.selectCity(cityId)!!

            val cities = AdventureCityPersistence(city.adventureId).selectCities()

            val headCity = cities[cities.indexOf(city) - 1]

            val originCode = AviaSalesAPI().getCityCode(headCity.name)
            val destinationCode = AviaSalesAPI().getCityCode(city.name)
            val tickets = AviaSalesAPI().search(originCode!!, destinationCode!!, TimeUtils.toTicketString(city.startTime))

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateText(tickets),
                replyMarkup = KeyboardUtils.generateAviaButtons(tickets!!, cityId),
            )
        }
    }

    fun generateText(tickets: SearchResponse?): String {
        if (tickets == null) {
            return "Билеты не найдены."
        }
        var stringBuilder = ""
        stringBuilder += "Найденные билеты: \n"
        for (ticket in tickets.prices) {
            stringBuilder += "Откуда: ${ticket.origin}. Куда: ${ticket.destination}. Вылет: ${ticket.depart_date}.\n"
            stringBuilder += "Время полета: ${ticket.duration}. Цена: ${ticket.price}₽\n"
            stringBuilder += "----\n"
        }
        Logger.logger.info(stringBuilder)
        return stringBuilder
    }
}
