package online.k0ras1k.travelagent.controller.callback.adventure

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.api.geoapify.GeoapifyAPI
import online.k0ras1k.travelagent.api.geoapify.data.CoordinatesData
import online.k0ras1k.travelagent.data.models.AdventureData
import online.k0ras1k.travelagent.database.persistence.AdventureCityPersistence
import online.k0ras1k.travelagent.database.persistence.AdventurePersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class ShowFullRouteHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val adventureId: Int = callbackQuery.data.split("-")[3].toInt()

            val adventureData: AdventureData = AdventurePersistence().select(adventureId)!!
            val cities = AdventureCityPersistence(adventureId).selectCities()

            val api = GeoapifyAPI()

            val cityCoordinates =
                cities.map { cityName ->
                    val geoResult = api.findByText(cityName.name)
                    val filteredResult = api.filterList(geoResult)
                    CoordinatesData.getFromList(filteredResult[0].geometry.coordinates)
                }

            val routeUuid =
                api.getFile(
                    api.getMapLink(
                        api.generateMapRequestString(
                            api.getRoute(cityCoordinates),
                        ),
                        cityCoordinates,
                    ),
                )

            bot.sendDocument(
                chatId = ChatId.fromId(chatId),
                document = TelegramFile.ByFile(routeUuid),
                replyMarkup = KeyboardUtils.generateRouteButton(),
            )
        }
    }
}
