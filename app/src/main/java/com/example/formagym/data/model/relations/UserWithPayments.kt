package com.example.formagym.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.formagym.data.model.Payment
import com.example.formagym.data.model.User

data class UserWithPayments(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val payments: List<Payment>
)