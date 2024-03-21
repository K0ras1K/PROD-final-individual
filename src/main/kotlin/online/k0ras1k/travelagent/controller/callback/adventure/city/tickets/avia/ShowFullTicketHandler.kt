package online.k0ras1k.travelagent.controller.callback.adventure.city.tickets.avia

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.api.aviasales.AviaSalesAPI
import online.k0ras1k.travelagent.api.aviasales.data.SearchResponse
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class ShowFullTicketHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()
            val ticketId: Int = callbackQuery.data.split("-")[3].toInt() - 1

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
                text = generateText(tickets, ticketId),
                parseMode = ParseMode.MARKDOWN,
                replyMarkup =
                    InlineKeyboardMarkup.create(
                        listOf(InlineKeyboardButton.Url("Купить", "https://aviasales.ru${tickets!!.prices[ticketId].link}")),
                        listOf(KeyboardUtils.getBackButton()),
                    ),
            )
        }
    }

    fun generateText(
        tickets: SearchResponse?,
        ticketId: Int,
    ): String {
        if (tickets == null) {
            return "Билет не найден."
        }
        val ticket = tickets.prices[ticketId]
        val stringBuilder =
            """
            **Билет** ${ticketId + 1}
            **Откуда**: ${ticket.origin}
            **Куда**: ${ticket.destination}
            **Время вылета**: ${ticket.depart_date}
            **Билет найден**: ${ticket.found_at}
            **Дитснация полёта**: ${ticket.distance}
            **Время полёта**: ${ticket.duration}М.
            **Цена**: ${ticket.price}₽
            """.trimIndent()
        println(stringBuilder)
        return stringBuilder
    }
}
