package online.k0ras1k.travelagent.controller

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.UserData
import online.k0ras1k.travelagent.database.persistence.UserPersistence
import online.k0ras1k.travelagent.database.redis.StatusMachine
import online.k0ras1k.travelagent.templates.MainMessage

class HelpController(private val message: Message, private val bot: Bot) {
    fun handleHelp() {
        runBlocking {
            if (UserPersistence(
                    UserData(
                        id = null,
                        name = message.chat.firstName!!,
                        tgLogin = message.chat.username!!,
                        tgId = message.chat.id,
                    ),
                ).select() == null
            ) {
                UserPersistence(
                    UserData(
                        id = null,
                        name = message.chat.firstName!!,
                        tgLogin = message.chat.username!!,
                        tgId = message.chat.id,
                    ),
                ).insert()
            }

            StatusMachine.removeStatus(message.chat.id)
            MainMessage(bot, message.chat.id, 0).sendHeadMessage()
        }
    }
}
