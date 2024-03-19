package online.k0ras1k.travelagent.utils

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import online.k0ras1k.travelagent.api.aviasales.data.SearchResponse
import online.k0ras1k.travelagent.data.models.AdventureCityData
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
                    text = "\uD83C\uDF0D Путешествия",
                    callbackData = "show-adventures",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    text = "⁉\uFE0F FAQ",
                    callbackData = "show-faq",
                ),
            ),
        )
    }

    fun generateAdventuresKeyboard(adventures: List<AdventureData>): InlineKeyboardMarkup {
        val onLineCount = 3
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        text = "\uD83C\uDFDD\uFE0F Создать путешествие",
                        callbackData = "create-adventure",
                    ),
                ),
            )

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
                    "\uD83D\uDE4B Пригласить друга",
                    "add-friend-adventure-${adventureData.id}",
                ),
            ),
            listOf(getBackButton()),
        )
    }

    fun generateCitiesButtons(
        cities: List<AdventureCityData>,
        adventureId: Int,
    ): InlineKeyboardMarkup {
        val onLineCount = 5
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        text = "\uD83C\uDF06 Добавить город",
                        callbackData = "add-city-$adventureId",
                    ),
                ),
            )

        var tempRows: MutableList<InlineKeyboardButton.CallbackData> = mutableListOf()
        var counter = 0
        for (city in cities) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    text = "$counter. ${city.name}",
                    callbackData = "edit-city-${city.id}",
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

    fun generateCityButton(cityData: AdventureCityData): InlineKeyboardMarkup {
        return InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(
                    "✈\uFE0F Авиабилеты",
                    "find-avia-${cityData.id}",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    "\uD83D\uDE82 ЖД Билеты",
                    "find-zd-${cityData.id}",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    "\uD83C\uDFE0 Отели",
                    "find-hotels-${cityData.id}",
                ),
            ),
            listOf(getBackButton()),
        )
    }

    fun generateAviaButtons(tickets: SearchResponse): InlineKeyboardMarkup {
        val onLineCount = 4
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (ticket in tickets.prices) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    callbackData = ticket.depart_date,
                    text = "$counter.",
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
        println(buttons)
        return InlineKeyboardMarkup.create(buttons)
    }

    private fun getBackButton(): InlineKeyboardButton.CallbackData {
        return InlineKeyboardButton.CallbackData("\uD83D\uDD19 Назад", "back")
    }
}
