package online.k0ras1k.travelagent.controller.callback.adventure.city.route

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Logger
import online.k0ras1k.travelagent.api.geoapify.GeoapifyAPI
import online.k0ras1k.travelagent.api.geoapify.data.CoordinatesData
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class ShowRouteHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val cityId: Int = callbackQuery.data.split("-")[2].toInt()

            val persistence = AdventureCityPersistence(0)
            val city = persistence.selectCity(cityId)!!

            val cities = AdventureCityPersistence(city.adventureId).selectCities()

            val headCity = cities[cities.indexOf(city) - 1]
            val api = GeoapifyAPI()

            Logger.logger.info("Before route")
            val routeUuid =
                api.getFile(
                    api.getMapLink(
                        api.generateMapRequestString(
                            api.getRoute(
                                listOf(
                                    CoordinatesData.getFromList(api.filterList(api.findByText(headCity.name))[0].geometry.coordinates),
                                    CoordinatesData.getFromList(api.filterList(api.findByText(city.name))[0].geometry.coordinates),
                                ),
                            ),
                        ),
                    ),
                )

            Logger.logger.info(routeUuid.path)
            bot.sendDocument(
                chatId = ChatId.fromId(chatId),
                document = TelegramFile.ByFile(routeUuid),
                replyMarkup = KeyboardUtils.generateRouteButton(),
            )
        }
    }
}
