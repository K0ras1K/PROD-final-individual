package online.k0ras1k.travelagent.utils

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

object KeyboardUtils {

    fun generateMainInlineKeyboard(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup.create(
            listOf(InlineKeyboardButton.CallbackData(text = "Заполнить информацию о себе", callbackData = "extend-information"))
        )
    }

}