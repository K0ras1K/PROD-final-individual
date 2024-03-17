package online.k0ras1k.travelagent

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.extensions.filters.Filter
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.controller.HelpController
import online.k0ras1k.travelagent.controller.TextController
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.database.DatabaseFactory
import online.k0ras1k.travelagent.database.redis.StatusMachine
import org.jetbrains.exposed.sql.Database
import org.w3c.dom.Text

fun main() {
    val db =
        Database.connect(
            DatabaseFactory.createHikariDataSource(
                "jdbc:postgresql://postgres/travelagent",
                "org.postgresql.Driver",
                "K0ras1K",
                "Shah!9Sah@",
            ),
        )

    val bot =
        bot {
            token = dotenv()["TELEGRAM_BOT_TOKEN"]
//            logLevel = LogLevel.Network.Body

            dispatch {
                command("start") {
                    HelpController(message, bot).handleHelp()
                }

                callbackQuery("extend-information") {
                    runBlocking {
                        val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
                        val headMessage = callbackQuery.message?.messageId ?: return@runBlocking

                        StatusMachine.setStatus(chatId, TextStatus.OLD_TYPE, headMessage)
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
