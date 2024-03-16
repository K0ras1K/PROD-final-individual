package online.k0ras1k.travelagent

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.logging.LogLevel
import io.github.cdimascio.dotenv.dotenv
import online.k0ras1k.travelagent.data.models.UserData
import online.k0ras1k.travelagent.database.DatabaseFactory
import online.k0ras1k.travelagent.database.persistence.UserPersistence
import org.jetbrains.exposed.sql.Database


fun main() {

    val db = Database.connect(
        DatabaseFactory.createHikariDataSource(
            "jdbc:postgresql://postgres/travelagent",
            "org.postgresql.Driver",
            "K0ras1K",
            "Shah!9Sah@"
        ),
    )

    Initialization.initialize()

     val bot = bot {
         token = dotenv()["TELEGRAM_BOT_TOKEN"]
         logLevel = LogLevel.Network.Body


         dispatch {
             command("start") {
                 UserPersistence(
                     UserData(
                         id = null,
                         name = message.chat.firstName!!,
                         tgLogin = message.chat.username!!,
                         tgId = message.chat.id
                     )
                 ).insert()
                 val result = bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Hi there!")
                 result.fold({
                     // do something here with the response
                 },{
                     // do something with the error
                 })
             }
         }
     }
    bot.startPolling()
}