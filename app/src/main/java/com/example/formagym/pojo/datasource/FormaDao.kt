package com.example.formagym.pojo.datasource

import androidx.room.*
import com.example.formagym.pojo.model.Payment
import com.example.formagym.pojo.model.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FormaDao {

    @Transaction
    suspend fun saveUserWithPayment(user: User, payment: Payment) {
        saveUser(user)
        savePayment(payment)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveUser(user: User)
    @Delete
    abstract suspend fun removeUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun savePayment(payment: Payment)

    @Query("SELECT * FROM Payment")
    abstract suspend fun getAllPayments(): List<Payment>

    @Query("SELECT SUM(moneyPaid) FROM Payment")
    abstract suspend fun getTotalIncome(): Int

    @Query("SELECT AVG(moneyPaid) FROM Payment")
    abstract suspend fun getAvgIncome(): Double

    @Query("SELECT * FROM members_table WHERE subscribeEndDate > :currentDate")
    abstract suspend fun getActiveMembers(currentDate: Long): List<User>

    @Query("SELECT COUNT(subscribeEndDate) FROM members_table WHERE subscribeEndDate > :currentDate")
    abstract suspend fun getActiveMembersCount(currentDate: Long): Int

    @Query("SELECT * FROM members_table WHERE subscribeEndDate <= :currentDate")
    abstract suspend fun getInActiveMembers(currentDate: Long): List<User>

    @Query("SELECT COUNT(subscribeEndDate) FROM members_table WHERE subscribeEndDate <= :currentDate")
    abstract suspend fun getInactiveMembersCount(currentDate: Long): Int


    @Query("SELECT * FROM members_table WHERE subscribeEndDate > :currentDate AND name LIKE :searchQuery")
    abstract fun searchActiveMembers(currentDate: Long, searchQuery: String): Flow<List<User>>

    @Query("SELECT * FROM members_table WHERE subscribeEndDate <= :currentDate AND name LIKE :searchQuery")
    abstract fun searchInActiveMembers(currentDate: Long, searchQuery: String): Flow<List<User>>
}