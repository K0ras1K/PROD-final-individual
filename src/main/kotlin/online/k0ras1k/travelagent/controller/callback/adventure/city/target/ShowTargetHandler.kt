package online.k0ras1k.travelagent.controller.callback.adventure.city.target

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.TargetData
import online.k0ras1k.travelagent.database.persistence.TargetPersistence
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.TimeUtils

class ShowTargetHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val targetId: Int = callbackQuery.data.split("-")[2].toInt()

            val targetData = TargetPersistence().selectById(targetId)!!

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = generateText(targetData),
                replyMarkup = InlineKeyboardMarkup.create(listOf(KeyboardUtils.getBackButton())),
            )
        }
    }

    private fun generateText(targetData: TargetData): String {
        return """
            ${targetData.name}
            $${targetData.receipt}
            ${TimeUtils.toTargetTime(targetData.date)}, ${targetData.time}
            
            ${targetData.description}
            """.trimIndent()
    }
}
