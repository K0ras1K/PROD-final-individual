package online.k0ras1k.travelagent.controller

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.AdventureInviteData
import online.k0ras1k.travelagent.data.models.UserData
import online.k0ras1k.travelagent.database.persistence.AdventureInvitesPersistence
import online.k0ras1k.travelagent.database.persistence.AdventurePersistence
import online.k0ras1k.travelagent.database.persistence.UserPersistence
import online.k0ras1k.travelagent.database.redis.StatusMachine
import online.k0ras1k.travelagent.templates.MainMessage
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.UsersUtils

class HelpController(private val message: Message, private val bot: Bot) {
    fun handleHelp() {
        runBlocking {
            handleInvite()
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

    private fun handleInvite() {
        if (message.text!!.split(" ").size > 1) {
            val extendedData = message.text!!.split(" ")[1]
            val adventureId = extendedData.split("-")[1]
            val adventureData = AdventurePersistence().select(adventureId.toInt())
            val users = AdventureInvitesPersistence().selectByAdventure(adventureId.toInt()) + adventureData!!.createdBy

            users.forEach {
                bot.sendMessage(
                    ChatId.fromId(it),
                    text = "Пользоветель ${UsersUtils.getUsernameById(
                        message.chat.id,
                    )} присоединился к путешествию ${adventureData!!.name}",
                    replyMarkup = KeyboardUtils.generateRouteButton(),
                )
            }
            AdventureInvitesPersistence().insert(
                AdventureInviteData(
                    adventureId = adventureId.toInt(),
                    invitedUser = message.chat.id,
                ),
            )
        }
    }
}
