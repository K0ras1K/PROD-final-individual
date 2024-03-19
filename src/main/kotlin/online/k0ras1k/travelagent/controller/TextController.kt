package online.k0ras1k.travelagent.controller

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.database.persistence.AdventurePersistence
import online.k0ras1k.travelagent.database.persistence.ExtendedUserPersistence
import online.k0ras1k.travelagent.database.redis.StatusMachine
import online.k0ras1k.travelagent.utils.KeyboardUtils

class TextController(private val message: Message, private val bot: Bot) {
    fun handleMessages() {
        runBlocking {
            val statusData = StatusMachine.getStatus(message.chat.id) ?: return@runBlocking

            if (statusData.status == TextStatus.OLD_TYPE) {
                val headMessage = statusData.headMessage
                StatusMachine.setStatus(
                    message.chat.id,
                    StatusData(
                        status = TextStatus.BIO_TYPE,
                        headMessage = headMessage,
                        data = mutableListOf(message.text.toString()),
                    ),
                )
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Введите информацию о себе",
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
            }
            if (statusData.status == TextStatus.BIO_TYPE) {
                val headMessage = statusData.headMessage
                StatusMachine.setStatus(
                    message.chat.id,
                    StatusData(
                        status = TextStatus.CITY_TYPE,
                        headMessage = headMessage,
                        data = (statusData.data + message.text.toString()).toMutableList(),
                    ),
                )
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Введите свой город",
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
            }
            if (statusData.status == TextStatus.CITY_TYPE) {
                val headMessage = statusData.headMessage
                val persistence = ExtendedUserPersistence(message.chat.id)

                val targetStatusData =
                    StatusData(
                        status = statusData.status,
                        headMessage = statusData.headMessage,
                        data = (statusData.data + message.text.toString()).toMutableList(),
                    )

                if (persistence.select() == null) {
                    persistence.insert(targetStatusData)
                } else {
                    persistence.update(targetStatusData)
                }

                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Hi there!",
                    replyMarkup = KeyboardUtils.generateMainInlineKeyboard(),
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)

                StatusMachine.removeStatus(message.chat.id)
            }

            if (statusData.status == TextStatus.ADVENTURE_NAME) {
                val headMessage = statusData.headMessage
                val persistence = AdventurePersistence()

                val targetStatusData =
                    StatusData(
                        status = statusData.status,
                        headMessage = statusData.headMessage,
                        data = (statusData.data + message.text.toString()).toMutableList(),
                    )

                persistence.updateName(
                    id = targetStatusData.data[0].toInt(),
                    name = targetStatusData.data[1],
                )

                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Hi there!",
                    replyMarkup = KeyboardUtils.generateMainInlineKeyboard(),
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)

                StatusMachine.removeStatus(message.chat.id)
            }

            if (statusData.status == TextStatus.ADVENTURE_DESCRIPTION) {
                val headMessage = statusData.headMessage
                val persistence = AdventurePersistence()

                val targetStatusData =
                    StatusData(
                        status = statusData.status,
                        headMessage = statusData.headMessage,
                        data = (statusData.data + message.text.toString()).toMutableList(),
                    )

                persistence.updateDescription(
                    id = targetStatusData.data[0].toInt(),
                    description = targetStatusData.data[1],
                )

                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Hi there!",
                    replyMarkup = KeyboardUtils.generateMainInlineKeyboard(),
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)

                StatusMachine.removeStatus(message.chat.id)
            }
        }
    }
}
