package com.example.cryptonomicon.dao

import android.content.Context
import androidx.room.*
import com.example.cryptonomicon.models.Converters
import com.example.cryptonomicon.models.TokenDetails

@Database(entities = [TokenDetails::class], version = 1)
@androidx.room.TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tokenDetailsDao(): TokenDetailsDao

    companion object {

        @Volatile
        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            INSTANCE = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "crypto-database"
            ).build()

            return INSTANCE
        }
    }

}