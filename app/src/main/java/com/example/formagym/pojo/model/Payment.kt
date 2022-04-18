package com.example.formagym.pojo.model

import android.content.Context
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.formagym.Constants
import kotlin.math.ceil

@Entity
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val paymentId: Int? = null,
    val date: Long = System.currentTimeMillis(),
    val moneyPaid: Int,
    var userId: Int,
    val userName: String
) {
    companion object {
        fun fromDays(userName: String, userId: Int, timeInMilli: Long): Payment {
            val days = ceil((timeInMilli.minus(System.currentTimeMillis())).toDouble() / (Constants.DAY_IN_MILLI_SEC))
            Log.d("FromDays", "fromDays: $days")
            val price = (days * Constants.DAY_PRICE).toInt()
            return Payment(moneyPaid = price, userId = userId, userName = userName)
        }

        fun fromDays(userName: String, userId: Int, timeInMilli: Long, paymentPrice: Double): Payment {
            val days = ceil((timeInMilli.minus(System.currentTimeMillis())).toDouble() / (Constants.DAY_IN_MILLI_SEC))
            Log.d("FromDays", "fromDays: $days")
            val price = (days * paymentPrice).toInt()
            return Payment(moneyPaid = price, userId = userId, userName = userName)
        }

        fun fromMonths(userName: String, userId: Int, monthsCount: Int): Payment {
            val price = monthsCount.times(Constants.MONTH_PRICE)
            return Payment(
                moneyPaid = price,
                userId = userId,
                userName = userName
            )
        }
    }
}
