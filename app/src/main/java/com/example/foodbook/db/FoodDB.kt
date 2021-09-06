package com.example.foodbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodbook.FoodViewActivity
import com.example.foodbook.dao.FoodDAO
import com.example.foodbook.entity.Food

@Database(
    entities = [(Food::class)],
    version = 2
)

abstract class FoodDB : RoomDatabase() {
    abstract fun getFoodDAO() :FoodDAO

    companion object {
        @Volatile
        private var instance: FoodDB? = null

        fun getInstance(context: FoodViewActivity): FoodDB {
            if (instance == null) {
                synchronized(FoodDB::class) {
                    instance = buildDatabase(context)

                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context): FoodDB {
            return Room.databaseBuilder(
                context.applicationContext,
                FoodDB::class.java,
                "FoodDB"
            ).fallbackToDestructiveMigration().build()
        }


    }
}