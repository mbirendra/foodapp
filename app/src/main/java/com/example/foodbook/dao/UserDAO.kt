package com.example.foodbook.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodbook.entity.User

@Dao
interface UserDAO {
    //insert User

    @Insert
   suspend fun registerUser(user: User)

   @Query("select * from User where username = :username and password = :password")
    suspend fun authenticate(username : String, password : String) :User

}