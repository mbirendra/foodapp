package com.example.foodwearos.entity

import androidx.room.Entity

@Entity
class Food (
    var _id:String="",
    var recipetitle : String? = null,
    var recipeimg : String? = null,
    var recipedesc : String? = null,
    var foodcategory : String? = null,
    var foodtype:String?=null,
    var preptime:String?=null,
)