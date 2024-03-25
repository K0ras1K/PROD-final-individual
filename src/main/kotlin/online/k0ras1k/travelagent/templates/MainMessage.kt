package online.k0ras1k.travelagent.templates

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import online.k0ras1k.travelagent.database.persistence.ExtendedUserPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils

class MainMessage(val bot: Bot, val chatId: Long, val messageId: Long) {
    fun toHeadMessage() {
        val flag = ExtendedUserPersistence(chatId).select() != null

        bot.editMessageText(
            chatId = ChatId.fromId(chatId),
            messageId = messageId,
            text =
                """
                Добро пожаловать в нашего умного путеводителя! 🌍✈️

                Готовы отправиться в незабываемое приключение? С нашим ботом вы сможете спланировать идеальное путешествие со своими друзьями, минуя головную боль о бронировании билетов, отелей и планировании маршрутов.

                Позвольте нам взять на себя заботу о каждой детали: от выбора маршрута до подбора развлечений и учета погодных условий. Вместе мы сделаем ваше путешествие незабываемым и беззаботным!

                Просто начните общение с ботом, и мы возьмем на себя все остальное. Путешествуйте с удовольствием! 🚀🌟
                """.trimIndent(),
            replyMarkup = KeyboardUtils.generateMainInlineKeyboard(flag),
        )
    }

    fun sendHeadMessage() {
        val flag = ExtendedUserPersistence(chatId).select() != null
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text =
                """
                Добро пожаловать в нашего умного путеводителя! 🌍✈️

                Готовы отправиться в незабываемое приключение? С нашим ботом вы сможете спланировать идеальное путешествие со своими друзьями, минуя головную боль о бронировании билетов, отелей и планировании маршрутов.

                Позвольте нам взять на себя заботу о каждой детали: от выбора маршрута до подбора развлечений и учета погодных условий. Вместе мы сделаем ваше путешествие незабываемым и беззаботным!

                Просто начните общение с ботом, и мы возьмем на себя все остальное. Путешествуйте с удовольствием! 🚀🌟
                """.trimIndent(),
            replyMarkup = KeyboardUtils.generateMainInlineKeyboard(flag),
        )
    }
}
