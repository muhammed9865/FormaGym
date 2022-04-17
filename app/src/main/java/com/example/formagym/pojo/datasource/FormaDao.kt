package com.example.formagym.pojo.datasource

import androidx.room.*
import com.example.formagym.pojo.model.Payment
import com.example.formagym.pojo.model.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FormaDao {

    @Transaction
    open suspend fun saveUserWithPayment(user: User, payment: Payment) {
        saveUser(user)
        if (payment.userId == -1) {
            payment.userId = getLastInsertedMemberId()
        }
        savePayment(payment)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveUser(user: User)

    @Query("SELECT * FROM members_table WHERE id = :userId")
    abstract suspend fun getUserByID(userId: Int): User?
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


    @Query("SELECT MAX(id) FROM members_table")
    protected abstract suspend fun getLastInsertedMemberId(): Int

}