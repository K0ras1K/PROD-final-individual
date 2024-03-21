package online.k0ras1k.travelagent

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.logging.LogLevel
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.controller.HelpController
import online.k0ras1k.travelagent.controller.TextController
import online.k0ras1k.travelagent.controller.callback.BackHandler
import online.k0ras1k.travelagent.controller.callback.adventure.CreateAdventureHandler
import online.k0ras1k.travelagent.controller.callback.adventure.FullAdventureHandler
import online.k0ras1k.travelagent.controller.callback.adventure.ShowAdventureHandler
import online.k0ras1k.travelagent.controller.callback.adventure.city.CitiesAdventureHandler
import online.k0ras1k.travelagent.controller.callback.adventure.city.ShowCityHandler
import online.k0ras1k.travelagent.controller.callback.adventure.city.ShowSightsHandler
import online.k0ras1k.travelagent.controller.callback.adventure.city.tickets.avia.ShowAviaTicketHandler
import online.k0ras1k.travelagent.controller.callback.adventure.city.tickets.avia.ShowFullTicketHandler
import online.k0ras1k.travelagent.controller.callback.adventure.city.tickets.hotel.ShowHotelHandler
import online.k0ras1k.travelagent.controller.callback.adventure.city.tickets.zd.ShowZDTicketHandler
import online.k0ras1k.travelagent.controller.callback.adventure.edit.EditAdventureDescriptionHandler
import online.k0ras1k.travelagent.controller.callback.adventure.edit.EditAdventureNameHandler
import online.k0ras1k.travelagent.controller.callback.adventure.edit.city.AddCityHandler
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.DatabaseFactory
import online.k0ras1k.travelagent.database.redis.StatusMachine
import org.jetbrains.exposed.sql.Database

fun main() {
    while (true) {
        try {
            connect()
            break
        } catch (exception: Exception) {
            Logger.logger.error("Нет подключения в БД")
        }
    }

    val bot =
        bot {
            token = dotenv()["TELEGRAM_BOT_TOKEN"]
            logLevel = LogLevel.Network.Body

            dispatch {
                command("start") {
                    HelpController(message, bot).handleHelp()
                }

                callbackQuery("create-adventure") {
                    CreateAdventureHandler(callbackQuery, bot).handle()
                }

                callbackQuery("show-adventures") {
                    ShowAdventureHandler(callbackQuery, bot).handle()
                }

                callbackQuery("back") {
                    BackHandler(callbackQuery, bot).handle()
                }

                callbackQuery {
                    val data = callbackQuery.data
                    if (data.startsWith("full-adventure-")) {
                        FullAdventureHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("change-adventure-name-")) {
                        EditAdventureNameHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("change-adventure-description-")) {
                        EditAdventureDescriptionHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("cities-adventure-")) {
                        CitiesAdventureHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("add-city-")) {
                        AddCityHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("edit-city-")) {
                        ShowCityHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("find-avia-")) {
                        ShowAviaTicketHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("show-ticket-")) {
                        ShowFullTicketHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("find-zd-")) {
                        ShowZDTicketHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("find-hotels-")) {
                        ShowHotelHandler(callbackQuery, bot).handle()
                    }
                    if (data.startsWith("find-sight-")) {
                        ShowSightsHandler(callbackQuery, bot).handle()
                    }
                }

                callbackQuery("extend-information") {
                    runBlocking {
                        val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
                        val headMessage = callbackQuery.message?.messageId ?: return@runBlocking

                        StatusMachine.setStatus(
                            chatId,
                            StatusData(
                                status = TextStatus.OLD_TYPE,
                                headMessage = headMessage,
                                data = mutableListOf(),
                            ),
                        )
                        bot.editMessageText(
                            chatId = ChatId.fromId(chatId),
                            messageId = headMessage,
                            text = "Введите свой возраст",
                        )
                    }
                }

                message(Filter.Text) {
                    TextController(message, bot).handleMessages()
                }
            }
        }

    Initialization.initialize()

    bot.startPolling()
}

fun connect() {
    val db =
        Database.connect(
            DatabaseFactory.createHikariDataSource(
                "jdbc:postgresql://postgres/travelagent",
                "org.postgresql.Driver",
                "K0ras1K",
                "Shah!9Sah@",
            ),
        )
}
