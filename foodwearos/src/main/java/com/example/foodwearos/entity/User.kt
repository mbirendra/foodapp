package com.example.foodwearos.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User (
    var fullname : String? = null,
    var email : String? = null,
    var username : String? = null,
    var password : String? = null
){
    @PrimaryKey(autoGenerate = true)
    var userId : Int = 0
}