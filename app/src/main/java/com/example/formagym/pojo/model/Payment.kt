package com.example.formagym.pojo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.formagym.Constants

@Entity
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val paymentId: Int? = null,
    val date: Long = System.currentTimeMillis(),
    val moneyPaid: Int,
    var userId: Int
) {
    companion object {
        fun fromDays(userId: Int, timeInMilli: Long): Payment {
            val days = timeInMilli.floorDiv(Constants.DAY_IN_MILLI_SEC)
            val price = (days * Constants.DAY_PRICE).toInt()
            return Payment(moneyPaid = price, userId = userId)
        }

        fun fromMonths(userId: Int, monthsCount: Int): Payment {
            val price = monthsCount.times(Constants.MONTH_PRICE)
            return Payment(
                moneyPaid = price,
                userId = userId
            )
        }
    }
}
