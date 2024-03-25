package online.k0ras1k.travelagent.database.persistence

import online.k0ras1k.travelagent.data.models.PaymentData
import online.k0ras1k.travelagent.data.models.PaymentRespondModel
import online.k0ras1k.travelagent.database.schemas.PaymentsTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PaymentPersistence {
    fun insert(paymentData: PaymentData) {
        try {
            transaction {
                PaymentsTable.insert {
                    it[PaymentsTable.userId] = paymentData.userId
                    it[PaymentsTable.name] = paymentData.name
                    it[PaymentsTable.cityId] = paymentData.cityId
                    it[PaymentsTable.count] = paymentData.count
                    it[PaymentsTable.targetPaymentId] = paymentData.targetId
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun select(paymentUUId: UUID): List<PaymentRespondModel> {
        return try {
            transaction {
                PaymentsTable.selectAll()
                    .where { PaymentsTable.targetPaymentId.eq(paymentUUId) }
                    .map {
                        PaymentRespondModel(
                            userId = it[PaymentsTable.userId],
                            count = it[PaymentsTable.count],
                        )
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }

    fun selectAll(cityId: Int): List<PaymentData> {
        return try {
            transaction {
                PaymentsTable.selectAll()
                    .where { PaymentsTable.cityId.eq(cityId) }
                    .map {
                        PaymentData(
                            id = it[PaymentsTable.id].value,
                            userId = it[PaymentsTable.userId],
                            name = it[PaymentsTable.name],
                            cityId = it[PaymentsTable.cityId],
                            count = it[PaymentsTable.count],
                            targetId = it[PaymentsTable.targetPaymentId],
                        )
                    }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            listOf()
        }
    }

    fun parseUnique(payments: List<PaymentData>): List<PaymentData> {
        return payments.distinctBy { it.name }
    }
}
