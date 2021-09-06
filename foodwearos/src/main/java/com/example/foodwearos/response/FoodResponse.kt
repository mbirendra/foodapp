package com.example.foodwearos.response

import com.example.foodwearos.entity.Food


data class FoodResponse (val success:Boolean?=null, val foods:MutableList<Food>?=null, val saved:Food?=null, val message:String?=null)