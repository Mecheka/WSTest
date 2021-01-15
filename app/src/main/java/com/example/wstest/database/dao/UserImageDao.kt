package com.example.wstest.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wstest.database.entiry.UserImageEntity

@Dao
interface UserImageDao {

    @Insert
    fun addUserImage(userImageEntity: UserImageEntity)

    @Query("SELECT * FROM user_image WHERE user_image.user_name = :userName")
    fun findUserImage(userName: String): UserImageEntity
}