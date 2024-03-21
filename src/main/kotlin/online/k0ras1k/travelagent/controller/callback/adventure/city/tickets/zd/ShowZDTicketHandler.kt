package online.k0ras1k.travelagent.controller.callback.adventure.city.tickets.zd

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Logger
import online.k0ras1k.travelagent.api.yandex.YandexAPI
import online.k0ras1k.travelagent.api.yandex.data.SegmentModel
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class ShowZDTicketHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0)
            val city = persistence.selectCity(cityId)!!

            val cities = AdventureCityPersistence(city.adventureId).selectCities()

            val headCity = cities[cities.indexOf(city) - 1]

            val originCode = YandexAPI().findStationCode(headCity.name)
            println("found origin code - $originCode")
            println("starting find dest code")
            val destinationCode = YandexAPI().findStationCode(city.name)
            println(originCode)
            println(destinationCode)
            val tickets = YandexAPI().getTickets(originCode, destinationCode, TimeUtils.toTicketString(city.startTime))

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateText(tickets),
                replyMarkup = KeyboardUtils.generateZDButtons(tickets, cityId),
            )
        }
    }

    fun generateText(tickets: List<SegmentModel>): String {
        if (tickets == null) {
            return "Билеты не найдены."
        }
        var stringBuilder = ""
        stringBuilder += "Найденные билеты: \n"
        for (segment in tickets) {
            stringBuilder += "${segment.thread.title}.\n"
            stringBuilder += "Из: ${segment.from.title}\n"
            stringBuilder += "В: ${segment.to.title}\n"
            stringBuilder += "Отправка: ${segment.departure}.\n"
            stringBuilder += "Прибытие: ${segment.arrival}.\n"
            stringBuilder += "-----------------------\n"
        }
        Logger.logger.info(stringBuilder)
        return stringBuilder
    }
}
