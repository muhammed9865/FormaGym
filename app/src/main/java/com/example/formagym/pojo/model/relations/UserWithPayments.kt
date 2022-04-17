package com.example.formagym.pojo.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.formagym.pojo.model.Payment
import com.example.formagym.pojo.model.User

data class UserWithPayments(
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val payments: List<Payment>
)