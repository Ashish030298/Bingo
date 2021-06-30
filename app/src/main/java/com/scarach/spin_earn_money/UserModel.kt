package com.scarach.spin_earn_money

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

data class UserModel(
    var id: String = "",
    var isOneTimeWithdrawal:Boolean = false,
    var userName: String = "",
    var userEmail: String = "",
    var userCoin: Int = 0,
    var userImageUrl: String = "",
    var userReferId: String = "",
    var joinReferId: String = "",
    var dailyBonus: String = "",
    val timeStamp: Timestamp? = null,

    )

data class Withdrawal(
    var userName: String = "",
    var userEmail: String = "",
    var withdrawalCoin: Int = 0,
    var withdrawalAccount: String ="",
    val timeStamp: Timestamp? = null,

    )

open class AdminWithdrawal(
    var name: String = "",
    var imgurl: String = "",
    var minimum: Int = 0,
    var max: Int = 0,

    )



@Keep
@IgnoreExtraProperties
open class Transaction(
    var coin: String = "",
    var time: String = "",
    var title: String = "",
)

data class Admin(
    var startAppClick: Int = 0,
    var applovinClick: Int = 0,
    var startAppImpression: Int = 0,
    var applovinImpression: Int = 0,
    var spin: Int = 0,
    var scratch: Int = 0,
    var applovinTaskCoin: Int = 0,
    var startAppTaskCoin: Int = 0,
    var maxWithdraw: Int = 0,
    var minimumWithdraw: Int = 0
)

data class Scratch(
    var scratchcoin: String = "",
    var isMask: Boolean = false,
)


