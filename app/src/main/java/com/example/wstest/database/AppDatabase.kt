package com.example.wstest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wstest.database.dao.UserDao
import com.example.wstest.database.dao.UserImageDao
import com.example.wstest.database.entiry.UserEntity
import com.example.wstest.database.entiry.UserImageEntity

@Database(entities = [UserEntity::class, UserImageEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "ws_test").build()
        }
    }

    abstract fun userDao(): UserDao

    abstract fun userImageDao(): UserImageDao
}