package com.example.wstest.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wstest.database.entiry.UserEntity

@Dao
interface UserDao {

    @Insert
    fun register(userEntity: UserEntity)

    @Query("SELECT * FROM user WHERE user.user_name = :userName")
    suspend fun findUser(userName: String): UserEntity

    @Query("SELECT * FROM user WHERE user.user_name = :userName AND user.password = :password")
    suspend fun login(userName: String, password: String): UserEntity
}