package com.example.foodwearos.api

import com.example.foodwearos.response.LoginResponse
import com.example.foodwearos.response.FoodResponse
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    @FormUrlEncoded
    @POST("/User/login")
    suspend fun checkUser(@Field("username")username : String, @Field("password")password : String) : Response<LoginResponse>

    @GET("recipe/allrec")
    suspend fun showAllRecipe(
        @Header("Authorization") token:String
    ):Response<FoodResponse>
}