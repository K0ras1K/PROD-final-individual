package online.k0ras1k.travelagent.controller.callback.adventure

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.utils.KeyboardUtils

class InviteAdventureHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val adventureId: Int = callbackQuery.data.split("-")[3].toInt()

            bot.sendMessage(
                chatId = ChatId.fromId(chatId),
                text =
                    """
                    Сгенерированна ссылка-приглашение.
                    Перешлите это сообщение другу или отправьте ссылку.
                    
                    https://t.me/ttagent_kk0ras1kk_bot?start=$chatId-$adventureId
                    """.trimIndent(),
                replyMarkup = KeyboardUtils.generateRouteButton(),
            )
        }
    }
}
