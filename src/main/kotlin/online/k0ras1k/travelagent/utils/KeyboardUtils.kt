package online.k0ras1k.travelagent.utils

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import online.k0ras1k.travelagent.api.aviasales.data.SearchResponse
import online.k0ras1k.travelagent.api.hotellook.data.Hotel
import online.k0ras1k.travelagent.api.yandex.data.SegmentModel
import online.k0ras1k.travelagent.data.models.*
import online.k0ras1k.travelagent.database.persistence.UserPersistence

object KeyboardUtils {
    fun generateMainInlineKeyboard(isAdded: Boolean): InlineKeyboardMarkup {
        when (isAdded) {
            true -> {
                return InlineKeyboardMarkup.create(
                    listOf(
                        InlineKeyboardButton.CallbackData(
                            text = "\uD83C\uDF0D Путешествия",
                            callbackData = "show-adventures",
                        ),
                    ),
                    listOf(
                        InlineKeyboardButton.CallbackData(
                            text = "Изменить информацию о себе",
                            callbackData = "extend-information",
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
            false -> {
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
        }
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

    fun generateTargetButtons(
        cityId: Int,
        targets: List<TargetData>,
    ): InlineKeyboardMarkup {
        val onLineCount = 2
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton.CallbackData> = mutableListOf()
        var counter = 0
        for (target in targets) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    text = "${target.name}",
                    callbackData = "show-target-${target.id}",
                )
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83C\uDF06 Добавить цель",
                        "add-target-$cityId",
                    ),
                ),
            )
        buttons += mutableListOf(mutableListOf(getBackButton()))
        return InlineKeyboardMarkup.create(buttons)
    }

    fun generateFullAdventureButtons(
        adventureData: AdventureData,
        cities: List<AdventureCityData>,
    ): InlineKeyboardMarkup {
        val onLineCount = 1
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton.CallbackData> = mutableListOf()
        var counter = 0
        for (city in cities) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    text = "${city.name}",
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

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83C\uDF06 Добавить город",
                        "add-city-${adventureData.id}",
                    ),
                ),
            )

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83D\uDCA0 Показать маршрут",
                        "show-full-route-${adventureData.id}",
                    ),
                ),
            )

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83E\uDE99 Расходы",
                        "show-pay-${adventureData.id}",
                    ),
                    InlineKeyboardButton.CallbackData(
                        "\uD83D\uDE4B Друзья",
                        "add-friend-adventure-${adventureData.id}",
                    ),
                ),
            )
        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83D\uDCDD Заметки",
                        "note-menu-${adventureData.id}",
                    ),
                    InlineKeyboardButton.CallbackData(
                        "✏\uFE0F Редактирование",
                        "change-full-adventure-${adventureData.id}",
                    ),
                ),
            )
        buttons += mutableListOf(mutableListOf(getBackButton()))
        return InlineKeyboardMarkup.create(buttons)
    }

    fun generateNotesManagerButtons(
        adventureId: Int,
        notes: List<NoteData>,
    ): InlineKeyboardMarkup {
        val onLineCount = 2
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83D\uDCDD Добавить заметку",
                        "add-note-$adventureId",
                    ),
                ),
            )

        var tempRows: MutableList<InlineKeyboardButton.CallbackData> = mutableListOf()
        var counter = 0
        for (note in notes) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    text = "$counter. ${note.name}",
                    callbackData = "show-note-${note.id}",
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

    fun generateNoteButtons(adventureId: Int): InlineKeyboardMarkup {
        return InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(
                    "\uD83D\uDC6A Публичная",
                    "add-final-note-PUBLIC-$adventureId",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    "\uD83D\uDDDD\uFE0F Приватная",
                    "add-final-note-PRIVATE-$adventureId",
                ),
            ),
        )
    }

    fun generateUsersButton(
        users: List<Long>,
        initiatorId: Long,
    ): InlineKeyboardMarkup {
        var usersLogins: MutableList<String> = mutableListOf()
        for (user in users) {
            val persistence = UserPersistence(UserData(0, "", "", user))
            usersLogins += persistence.select()!!.tgLogin
        }

        val onLineCount = 2
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton.CallbackData> = mutableListOf()
        var counter = 0
        for (user in usersLogins) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    text = user,
                    callbackData = "add-users-$initiatorId-${users[counter - 1]}",
                )
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Завершить",
                        callbackData = "finish-payment-$initiatorId",
                    ),
                ),
            )
        return InlineKeyboardMarkup.create(buttons)
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

    fun generatePaymentsButton(
        payments: List<PaymentData>,
        cityId: Int,
    ): InlineKeyboardMarkup {
        val onLineCount = 3
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton.CallbackData> = mutableListOf()
        var counter = 0
        for (payment in payments) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    text = "${payment.name}",
                    callbackData = "show|payment|${payment.targetId}",
                )
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        text = "\uD83D\uDCB7 Добавить трату",
                        callbackData = "add-payment-$cityId",
                    ),
                ),
            )

        buttons += mutableListOf(mutableListOf(getBackButton()))
        return InlineKeyboardMarkup.create(buttons)
    }

    fun generateRouteButton(): InlineKeyboardMarkup {
        return InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(
                    "❌ Удалить из чата",
                    "delete-route",
                ),
            ),
        )
    }

    fun generateEditAdventureButtons(adventureData: AdventureData): InlineKeyboardMarkup {
        return InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(
                    "\uD83D\uDCCC Изменить название",
                    "change-adventure-name-${adventureData.id}",
                ),
                InlineKeyboardButton.CallbackData(
                    "✏\uFE0F Изменить описание",
                    "change-adventure-description-${adventureData.id}",
                ),
            ),
            listOf(
                InlineKeyboardButton.CallbackData(
                    "❌ Удалить путешествие",
                    "delete-adventure-${adventureData.id}",
                ),
            ),
            listOf(
                getBackButton(),
            ),
        )
    }

    fun generateCityButton(
        cityData: AdventureCityData,
        targets: List<TargetData>,
    ): InlineKeyboardMarkup {
        val onLineCount = 2
        val buttons: MutableList<MutableList<InlineKeyboardButton.CallbackData>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton.CallbackData> = mutableListOf()
        var counter = 0
        for (target in targets) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    text = "${target.name}",
                    callbackData = "show-target-${target.id}",
                )
            if (tempRows.size == onLineCount) {
                buttons += tempRows
                tempRows = mutableListOf()
            }
        }
        if (tempRows.isNotEmpty()) {
            buttons += tempRows
        }

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83C\uDF06 Добавить цель",
                        "add-target-${cityData.id}",
                    ),
                ),
            )

        buttons +=
            mutableListOf(
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83D\uDCB5 Расходы",
                        "show-payments-${cityData.id}",
                    ),
                ),
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "⬆\uFE0F Маршрут",
                        "show-route-${cityData.id}",
                    ),
                    InlineKeyboardButton.CallbackData(
                        "\uD83C\uDFE0 Отели",
                        "find-hotels-${cityData.id}",
                    ),
                ),
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83D\uDE82 ЖД Билеты",
                        "find-zd-${cityData.id}",
                    ),
                    InlineKeyboardButton.CallbackData(
                        "✈\uFE0F Авиабилеты",
                        "find-avia-${cityData.id}",
                    ),
                ),
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83C\uDF54 Рестораны",
                        "show-restaurants-${cityData.id}",
                    ),
                    InlineKeyboardButton.CallbackData(
                        "☔ Прогноз погоды",
                        "show-weather-${cityData.id}",
                    ),
                ),
                mutableListOf(
                    InlineKeyboardButton.CallbackData(
                        "\uD83D\uDDFD Достопримечательности",
                        "find-sight-${cityData.id}",
                    ),
                    InlineKeyboardButton.CallbackData(
                        "❌ Удалить город",
                        "delete-city-${cityData.id}",
                    ),
                ),
            )
        buttons += mutableListOf(mutableListOf(getBackButton()))
        return InlineKeyboardMarkup.create(buttons)
    }

    fun generateHotelButtons(
        hotels: List<Hotel>,
        cityId: Int,
    ): InlineKeyboardMarkup {
        val onLineCount = 4
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (hotel in hotels) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    callbackData = "show-hotel-$cityId-$counter",
                    text = "$counter. ${hotel.hotelName}",
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

    fun generateAviaButtons(
        tickets: SearchResponse,
        cityId: Int,
    ): InlineKeyboardMarkup {
        val onLineCount = 4
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (ticket in tickets.prices) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    callbackData = "show-ticket-$cityId-$counter",
                    text = "$counter. ${ticket.price}₽",
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

    fun generateZDButtons(
        tickets: List<SegmentModel>,
        cityId: Int,
    ): InlineKeyboardMarkup {
        val onLineCount = 4
        val buttons: MutableList<MutableList<InlineKeyboardButton>> = mutableListOf()

        var tempRows: MutableList<InlineKeyboardButton> = mutableListOf()
        var counter = 0
        for (ticket in tickets) {
            counter += 1
            tempRows +=
                InlineKeyboardButton.CallbackData(
                    callbackData = "show-zd-$cityId-$counter",
                    text = "$counter. ${ticket.thread.title}",
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

    fun getBackButton(): InlineKeyboardButton.CallbackData {
        return InlineKeyboardButton.CallbackData("\uD83D\uDD19 Назад", "back")
    }
}
