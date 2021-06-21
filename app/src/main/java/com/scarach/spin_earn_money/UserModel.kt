package com.scarach.spin_earn_money

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

data class UserModel(
    var id: String = "",
    var userName: String = "",
    var userEmail: String = "",
    var userCoin: Int = 0,
    var userImageUrl: String = "",
    var userReferId: String = "",
    var dailyBonus: String = "",
    val timeStamp: Timestamp? = null,

    )

@Keep
@IgnoreExtraProperties
open class Transaction(
    var coin: String = "",
    var time: String = "",
    var title: String = "",
)

data class Admin(
    var click: Int = 0,
    var impression: Int = 0,
    var spin: Int = 0,
    var scratch: Int = 0,
)

data class Scratch(
    var id: String = "",
    var scratchcoin: String = "",
    var isMask: Boolean = false,
)


