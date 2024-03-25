package online.k0ras1k.travelagent.controller.callback.adventure

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ParseMode
import kotlinx.coroutines.runBlocking
import online.k0ras1k.travelagent.data.models.AdventureData
import online.k0ras1k.travelagent.data.models.PaymentData
import online.k0ras1k.travelagent.data.models.TargetData
import online.k0ras1k.travelagent.database.persistence.*
import online.k0ras1k.travelagent.utils.KeyboardUtils
import online.k0ras1k.travelagent.utils.UsersUtils

class ShowPayHandler(private val callbackQuery: CallbackQuery, private val bot: Bot) {
    fun handle() {
        runBlocking {
            val chatId = callbackQuery.message?.chat?.id ?: return@runBlocking
            val headMessage = callbackQuery.message?.messageId ?: return@runBlocking
            val adventureId: Int = callbackQuery.data.split("-")[2].toInt()

            val adventureData: AdventureData = AdventurePersistence().select(adventureId)!!
            val cities = AdventureCityPersistence(adventureId).selectCities()

            val allTargets: MutableList<TargetData> = mutableListOf()
            val allPayments: MutableList<PaymentData> = mutableListOf()
            var users =
                AdventureInvitesPersistence().selectByAdventure(adventureId)
            users += AdventurePersistence().select(adventureId)!!.createdBy

            for (city in cities) {
                allTargets += TargetPersistence().selectAll(city.id)
                allPayments += PaymentPersistence().selectAll(city.id)
            }

            val userSums: MutableMap<Long, Int> = HashMap()
            var totalSum: Int = 0

            for (user in users) {
                userSums[user] = 0
            }

            for (payment in allPayments) {
                val userId = payment.userId
                val count = payment.count

                userSums[userId] = userSums.getOrDefault(userId, 0) + count
            }

            val targetSum = allTargets.sumBy { it.receipt }

            totalSum = userSums.values.sum() + targetSum

            println(targetSum)
            val userCount = users.size
            val targetPerUser = targetSum / userCount

            for (user in users) {
                userSums[user] = userSums.getOrDefault(user, 0) + targetPerUser
            }

            val output = StringBuilder()
            for ((userId, sum) in userSums) {
                output.append("${UsersUtils.getUsernameById(userId)} - $$sum\n")
            }
            output.append("Всего - $${totalSum}\n")

            println(output.toString())

            bot.editMessageText(
                chatId = ChatId.fromId(chatId),
                messageId = headMessage,
                text = output.toString(),
                parseMode = ParseMode.HTML,
                replyMarkup = InlineKeyboardMarkup.create(listOf(KeyboardUtils.getBackButton())),
            )
        }
    }
}
