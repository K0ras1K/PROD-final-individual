package online.k0ras1k.travelagent.controller

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Logger
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.database.redis.StatusMachine
import online.k0ras1k.travelagent.utils.KeyboardUtils

class TextController(private val message: Message, private val bot: Bot) {
    fun handleMessages() {
        runBlocking {
            Logger.logger.info("handling message")
            val status = StatusMachine.getStatus(message.chat.id)
            Logger.logger.info("Status - '$status'")

            if (status == TextStatus.OLD_TYPE) {
                Logger.logger.info("Found OLD_TYPE")
                val headMessage = StatusMachine.getHeadMessage(message.chat.id)
                Logger.logger.info("Found Head message")
//                         StatusMachine.removeStatus(message.chat.id)
                StatusMachine.setStatus(message.chat.id, TextStatus.BIO_TYPE, headMessage!!)
                Logger.logger.info("Setted status")
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Введите информацию о себе",
                )
                Logger.logger.info("Edited message")
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
                Logger.logger.info("Deleted message")
            }
            if (status == TextStatus.BIO_TYPE) {
                Logger.logger.info("Found BIO_TYPE")
                val headMessage = StatusMachine.getHeadMessage(message.chat.id)
                Logger.logger.info("Found Head message")
                StatusMachine.setStatus(message.chat.id, TextStatus.CITY_TYPE, headMessage!!)
                Logger.logger.info("Setted status")
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Введите свой город",
                )
                Logger.logger.info("Edited message")
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
                Logger.logger.info("Deleted message")
            }
            if (status == TextStatus.CITY_TYPE) {
                Logger.logger.info("Found CITY_TYPE")
                val headMessage = StatusMachine.getHeadMessage(message.chat.id)
                Logger.logger.info("Found Head message")
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Hi there!",
                    replyMarkup = KeyboardUtils.generateMainInlineKeyboard(),
                )
                Logger.logger.info("Edited message")
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
                Logger.logger.info("Deleted message")

                StatusMachine.removeStatus(message.chat.id)
                HelpController(message, bot).handleHelp()
            }
        }
    }
}
