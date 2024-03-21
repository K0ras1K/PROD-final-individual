package online.k0ras1k.travelagent.api.rzd.data

import kotlinx.serialization.Serializable

@Serializable
data class TimetableResponse(
    val result: String,
    val tp: List<String>,
    val TransferSearchMode: String,
    val flFPKRoundBonus: Boolean,
    val AutoTransferMode: String,
    val discounts: Map<String, String>,
    val timestamp: String,
)

@Serializable
data class Tp(
    val from: String,
    val fromCode: Int,
    val where: Int,
    val whereCode: Int,
    val date: String,
    val noSeats: Boolean,
    val defShowTime: String,
    val state: String,
    val list: List<Train>,
)

@Serializable
data class Train(
    val number: String,
    val number2: String,
    val type: Int,
    val typeEx: Int,
    val depth: Int,
    val new: Boolean,
    val elReg: Boolean,
    val deferredPayment: Boolean,
    val varPrice: Boolean,
    val code0: Int,
    val code1: Int,
    val bEntire: Boolean,
    val trainName: String,
    val brandLogo: Boolean,
    val brand: String,
    val brandId: Int,
    val carrier: String,
    val route0: String,
    val route1: String,
    val routeCode0: Int,
    val routeCode1: Int,
    val trDate0: String,
    val trTime0: String,
    val station0: String,
    val station1: String,
    val date0: String,
    val time0: String,
    val date1: String,
    val time1: String,
    val timeInWay: String,
    val flMsk: Int,
    val train_id: Int,
    val cars: List<Car>,
    val test: String,
    val testSize: Int,
    val carTypes: List<CarType>,
    val disabledType: Boolean,
    val addGoods: Boolean,
    val addCompLuggage: Boolean,
    val addHandLuggage: Boolean,
)

@Serializable
data class Car(
    val carDataType: Int,
    val itype: Int,
    val type: String,
    val typeLoc: String,
    val freeSeats: Int,
    val pt: Int,
    val tariff: Int,
    val servCls: String,
)

@Serializable
data class CarType(
    val type: String,
    val itype: Int,
)
