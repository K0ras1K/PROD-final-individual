package online.k0ras1k.travelagent.controller

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.Logger
import online.k0ras1k.travelagent.data.enums.NoteMediaType
import online.k0ras1k.travelagent.data.enums.NoteStatus
import online.k0ras1k.travelagent.data.enums.TextStatus
import online.k0ras1k.travelagent.data.models.AdventureCityData
import online.k0ras1k.travelagent.data.models.NoteData
import online.k0ras1k.travelagent.data.models.StatusData
import online.k0ras1k.travelagent.data.models.TargetData
import online.k0ras1k.travelagent.database.persistence.*
import online.k0ras1k.travelagent.database.redis.StatusMachine
import online.k0ras1k.travelagent.templates.MainMessage
import online.k0ras1k.travelagent.utils.TimeUtils

class TextController(private val message: Message, private val bot: Bot) {
    fun handleMessages() {
        runBlocking {
            Logger.logger.info(message.toString())
            val statusData = StatusMachine.getStatus(message.chat.id) ?: return@runBlocking
            Logger.logger.info(statusData.toString())

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

                MainMessage(bot, message.chat.id, headMessage).toHeadMessage()
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

                MainMessage(bot, message.chat.id, headMessage).toHeadMessage()
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

                MainMessage(bot, message.chat.id, headMessage).toHeadMessage()
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)

                StatusMachine.removeStatus(message.chat.id)
            }

            if (statusData.status == TextStatus.ADVENTURE_CITY_ADD) {
                val headMessage = statusData.headMessage
                StatusMachine.setStatus(
                    message.chat.id,
                    StatusData(
                        status = TextStatus.ADVENTURE_START_TIME_ADD,
                        headMessage = headMessage,
                        data = (statusData.data + message.text.toString()).toMutableList(),
                    ),
                )
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Введите время прибытия (в формате dd.MM.yyyy HH:mm:ss)",
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
            }

            if (statusData.status == TextStatus.ADVENTURE_START_TIME_ADD) {
                val headMessage = statusData.headMessage
                StatusMachine.setStatus(
                    message.chat.id,
                    StatusData(
                        status = TextStatus.ADVENTURE_END_TIME_ADD,
                        headMessage = headMessage,
                        data = (statusData.data + TimeUtils.toMillis(message.text!!).toString()).toMutableList(),
                    ),
                )
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Введите время выезда (в формате dd.MM.yyyy HH:mm:ss)",
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
            }

            if (statusData.status == TextStatus.ADVENTURE_END_TIME_ADD) {
                val headMessage = statusData.headMessage

                val targetStatusData =
                    StatusData(
                        status = statusData.status,
                        headMessage = statusData.headMessage,
                        data = (statusData.data + TimeUtils.toMillis(message.text!!).toString()).toMutableList(),
                    )

                val persistence = AdventureCityPersistence(targetStatusData.data[0].toInt())

                persistence.insertCity(
                    AdventureCityData(
                        id = 0,
                        name = targetStatusData.data[1],
                        startTime = targetStatusData.data[2].toLong(),
                        endTime = targetStatusData.data[3].toLong(),
                        adventureId = targetStatusData.data[0].toInt(),
                    ),
                )

                MainMessage(bot, message.chat.id, headMessage).toHeadMessage()
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)

                StatusMachine.removeStatus(message.chat.id)
            }

            if (statusData.status == TextStatus.NOTE_ADD_NAME) {
                val headMessage = statusData.headMessage

                val targetStatusData =
                    StatusData(
                        status = TextStatus.NOTE_ADD_URL,
                        headMessage = statusData.headMessage,
                        data = (statusData.data + message.text!!).toMutableList(),
                    )

                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Отправьте изображение | файл | текст",
                )
                StatusMachine.setStatus(message.chat.id, targetStatusData)
            }

            if (statusData.status == TextStatus.NOTE_ADD_URL) {
                val headMessage = statusData.headMessage

                val notePersistence = NotePersistence()

                notePersistence.insert(
                    NoteData(
                        id = 0,
                        tgId = message.chat.id,
                        adventureId = statusData.data[0].toInt(),
                        noteUrl = message.text!!,
                        type = NoteMediaType.TEXT,
                        status = NoteStatus.valueOf(statusData.data[1]),
                        name = statusData.data[2],
                    ),
                )

                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
                MainMessage(bot, message.chat.id, headMessage).toHeadMessage()
                StatusMachine.removeStatus(message.chat.id)
            }

            // TARGET
            if (statusData.status == TextStatus.TARGET_ADD_NAME) {
                val headMessage = statusData.headMessage
                StatusMachine.setStatus(
                    message.chat.id,
                    StatusData(
                        status = TextStatus.TARGET_ADD_DATE,
                        headMessage = headMessage,
                        data = (statusData.data + message.text.toString()).toMutableList(),
                    ),
                )
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Укажите дату",
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
            }

            if (statusData.status == TextStatus.TARGET_ADD_DATE) {
                val headMessage = statusData.headMessage
                StatusMachine.setStatus(
                    message.chat.id,
                    StatusData(
                        status = TextStatus.TARGET_ADD_TIME,
                        headMessage = headMessage,
                        data = (statusData.data + TimeUtils.toMillisOnlyDate(message.text!!).toString()).toMutableList(),
                    ),
                )
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Укажите время",
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
            }

            if (statusData.status == TextStatus.TARGET_ADD_TIME) {
                val headMessage = statusData.headMessage
                StatusMachine.setStatus(
                    message.chat.id,
                    StatusData(
                        status = TextStatus.TARGET_ADD_RECEIPT,
                        headMessage = headMessage,
                        data = (statusData.data + message.text!!.toString()).toMutableList(),
                    ),
                )
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Укажите примерный чек в $",
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
            }

            if (statusData.status == TextStatus.TARGET_ADD_RECEIPT) {
                val headMessage = statusData.headMessage
                StatusMachine.setStatus(
                    message.chat.id,
                    StatusData(
                        status = TextStatus.TARGET_ADD_DESCRIPTION,
                        headMessage = headMessage,
                        data = (statusData.data + message.text!!.toString()).toMutableList(),
                    ),
                )
                bot.editMessageText(
                    chatId = ChatId.fromId(message.chat.id),
                    messageId = headMessage,
                    text = "Добавьте комментарий или прикрепите ссылку",
                )
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
            }

            if (statusData.status == TextStatus.TARGET_ADD_DESCRIPTION) {
                val headMessage = statusData.headMessage
                val persistence = TargetPersistence()

                val targetStatusData =
                    StatusData(
                        status = statusData.status,
                        headMessage = statusData.headMessage,
                        data = (statusData.data + message.text.toString()).toMutableList(),
                    )

                persistence.insert(
                    TargetData(
                        name = targetStatusData.data[1],
                        id = 0,
                        cityId = targetStatusData.data[0].toInt(),
                        createdAt = System.currentTimeMillis(),
                        date = targetStatusData.data[2].toLong(),
                        time = targetStatusData.data[3],
                        receipt = targetStatusData.data[4].toInt(),
                        description = targetStatusData.data[5],
                    ),
                )

                MainMessage(bot, message.chat.id, headMessage).toHeadMessage()
                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)

                StatusMachine.removeStatus(message.chat.id)
            }
        }
    }

    fun handleFiles() {
        runBlocking {
            Logger.logger.info(message.toString())
            val statusData = StatusMachine.getStatus(message.chat.id) ?: return@runBlocking
            Logger.logger.info(statusData.toString())

            if (statusData.status == TextStatus.NOTE_ADD_URL) {
                val headMessage = statusData.headMessage

                val notePersistence = NotePersistence()

                notePersistence.insert(
                    NoteData(
                        id = 0,
                        tgId = message.chat.id,
                        adventureId = statusData.data[0].toInt(),
                        noteUrl = message.document!!.fileId,
                        type = NoteMediaType.FILE,
                        status = NoteStatus.valueOf(statusData.data[1]),
                        name = statusData.data[2],
                    ),
                )

                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
                MainMessage(bot, message.chat.id, headMessage).toHeadMessage()
                StatusMachine.removeStatus(message.chat.id)
            }
        }
    }

    fun handlePhotos() {
        runBlocking {
            Logger.logger.info(message.toString())
            val statusData = StatusMachine.getStatus(message.chat.id) ?: return@runBlocking
            Logger.logger.info(statusData.toString())

            if (statusData.status == TextStatus.NOTE_ADD_URL) {
                val headMessage = statusData.headMessage

                val notePersistence = NotePersistence()

                notePersistence.insert(
                    NoteData(
                        id = 0,
                        tgId = message.chat.id,
                        adventureId = statusData.data[0].toInt(),
                        noteUrl = message.photo!![0].fileId,
                        type = NoteMediaType.PHOTO,
                        status = NoteStatus.valueOf(statusData.data[1]),
                        name = statusData.data[2],
                    ),
                )

                bot.deleteMessage(chatId = ChatId.fromId(message.chat.id), message.messageId)
                MainMessage(bot, message.chat.id, headMessage).toHeadMessage()
                StatusMachine.removeStatus(message.chat.id)
            }
        }
    }
}
