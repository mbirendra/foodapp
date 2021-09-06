package com.example.foodbook.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.foodbook.entity.Food

@Dao
interface FoodDAO {
    //insert Food

    @Insert
    suspend fun insertFood(Food: Food)

    @Query("select * from Food")
    suspend fun findAll() : List<Food>

    @Query("Delete from Food")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteFood(Food: Food)
}