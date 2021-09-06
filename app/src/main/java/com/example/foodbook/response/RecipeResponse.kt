package com.example.foodbook.response

import com.example.foodbook.entity.Food

data class FoodResponse (val success:Boolean?=null,val Foods:MutableList<Food>?=null,val saved:Food?=null,val message:String?=null)