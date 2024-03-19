package online.k0ras1k.travelagent.utils

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import online.k0ras1k.travelagent.data.models.AdventureData

object KeyboardUtils {
    fun generateMainInlineKeyboard(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(
                    text = "\uD83D\uDD87 Заполнить информацию о себе",
                    callbackData = "extend-information",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    text = "\uD83C\uDFDD\uFE0F Создать путешествие",
                    callbackData = "create-adventure",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    text = "\uD83C\uDF0D Мои путешествия",
                    callbackData = "show-adventures",
                ),
            ),
        )
    }

    fun generateAdventuresKeyboard(adventures: List<AdventureData>): InlineKeyboardMarkup {
        val onLineCount = 3
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton.CallbackData> = mutableListOf()
        var counter = 0
        for (adventure in adventures) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    text =
                        if (adventure.name != "") {
                            "$counter. ${adventure.name}"
                        } else {
                            "$counter. /черновик/"
                        },
                    callbackData = "full-adventure-${adventure.id}",
                )
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }
        buttons += mutableListOf(mutableListOf(getBackButton()))
        return InlineKeyboardMarkup.create(buttons)
    }

    fun generateFullAdventureButtons(adventureData: AdventureData): InlineKeyboardMarkup {
        return InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(
                    "▶\uFE0F Города",
                    "cities-adventure-${adventureData.id}",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    "\uD83D\uDCCC Изменить название",
                    "change-adventure-name-${adventureData.id}",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    "✏\uFE0F Изменить описание",
                    "change-adventure-description-${adventureData.id}",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    "\uD83C\uDFD9\uFE0F Добавить город",
                    "add-adventure-city-${adventureData.id}",
                ),
            ),
            listOf(getBackButton()),
        )
    }

    private fun getBackButton(): InlineKeyboardButton.CallbackData {
        return InlineKeyboardButton.CallbackData("\uD83D\uDD19 Назад", "back")
    }
}
