package com.example.wstest.database.entiry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "password") val password: String?,
    val name: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "card_no") val cardNo: String?,
    val phone: String,
)