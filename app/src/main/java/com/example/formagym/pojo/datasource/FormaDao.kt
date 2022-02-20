package com.example.formagym.pojo.datasource

import androidx.room.*
import com.example.formagym.pojo.model.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface FormaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(member: Member)
    @Delete
    suspend fun remove(member: Member)

    @Query("SELECT * FROM members_table")
    suspend fun getAll(): List<Member>

    @Query("SELECT * FROM members_table WHERE subscribeEndDate > :currentDate")
    suspend fun getActiveMembers(currentDate: Long): List<Member>

    @Query("SELECT * FROM members_table WHERE subscribeEndDate <= :currentDate")
    suspend fun getInActiveMembers(currentDate: Long): List<Member>

    @Query("SELECT * FROM members_table WHERE subscribeEndDate > :currentDate AND name LIKE :searchQuery")
    fun searchActiveMembers(currentDate: Long, searchQuery: String): Flow<List<Member>>

    @Query("SELECT * FROM members_table WHERE subscribeEndDate <= :currentDate AND name LIKE :searchQuery")
    fun searchInActiveMembers(currentDate: Long, searchQuery: String): Flow<List<Member>>
}