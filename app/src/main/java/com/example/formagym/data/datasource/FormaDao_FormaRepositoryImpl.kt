package com.example.formagym.data.datasource

import androidx.room.*
import com.example.formagym.data.model.Payment
import com.example.formagym.data.model.User
import com.example.formagym.data.model.relations.UserWithPayments
import com.example.formagym.domain.repository.FormaRepository
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FormaDao_FormaRepositoryImpl : FormaRepository {

    @Transaction
    override suspend fun saveUserWithPayment(user: User, payment: Payment) {
        saveUser(user)
        if (payment.userId == -1) {
            payment.userId = getLastInsertedMemberId()
        }
        savePayment(payment)
    }

    // User Related Methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun saveUser(user: User)

    @Query("SELECT * FROM members_table WHERE id = :userId")
    abstract override suspend fun getUserByID(userId: Int): User?

    @Delete
    abstract override suspend fun removeUser(user: User)

    @Query("SELECT * FROM members_table WHERE subscribeEndDate > :currentDate")
    abstract override suspend fun getActiveMembers(currentDate: Long): List<User>

    @Query("SELECT COUNT(subscribeEndDate) FROM members_table WHERE subscribeEndDate > :currentDate")
    abstract override suspend fun getActiveMembersCount(currentDate: Long): Int

    @Query("SELECT * FROM members_table WHERE subscribeEndDate <= :currentDate")
    abstract override suspend fun getInActiveMembers(currentDate: Long): List<User>

    @Query("SELECT COUNT(subscribeEndDate) FROM members_table WHERE subscribeEndDate <= :currentDate")
    abstract override suspend fun getInactiveMembersCount(currentDate: Long): Int


    @Query("SELECT * FROM members_table WHERE subscribeEndDate > :currentDate AND name LIKE :searchQuery")
    abstract override fun searchActiveMembers(currentDate: Long, searchQuery: String): Flow<List<User>>

    @Query("SELECT * FROM members_table WHERE subscribeEndDate <= :currentDate AND name LIKE :searchQuery")
    abstract override fun searchInActiveMembers(currentDate: Long, searchQuery: String): Flow<List<User>>


    // Payment Related Methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun savePayment(payment: Payment)

    @Query("SELECT * FROM Payment")
    abstract override suspend fun getAllPayments(): List<Payment>

    @Transaction
    @Query("SELECT * FROM members_table WHERE id = :userId")
    abstract override suspend fun getUserPayments(userId: Int): UserWithPayments

    @Query("SELECT SUM(moneyPaid) FROM Payment")
    abstract override suspend fun getTotalIncome(): Double?

    @Query("SELECT AVG(moneyPaid) FROM Payment")
    abstract override suspend fun getAvgIncome(): Double?

    @Query("SELECT AVG(moneyPaid) FROM Payment WHERE date BETWEEN :from AND :to")
    abstract override suspend fun getAvgIncomeBetweenTwoDates(from: Long, to: Long): Int?

    @Query("SELECT SUM(moneyPaid) FROM Payment WHERE date BETWEEN :from AND :to")
    abstract override suspend fun getTotalIncomeBetweenTwoDates(from: Long, to: Long): Int?

    @Query("DELETE FROM Payment")
    abstract override suspend fun deleteAllPayments()

    @Delete
    abstract override suspend fun deletePayment(payment: Payment)


    // Special Methods
    // Used with saving a new member to get his ID, and then insert it into the Payment.
    @Query("SELECT MAX(id) FROM members_table")
    protected abstract suspend fun getLastInsertedMemberId(): Int

}