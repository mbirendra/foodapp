package com.example.foodbook.api

import com.example.foodbook.entity.UserRegistration
import com.example.foodbook.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {
    //Register User
    @POST("/User/register")
    suspend fun registerUser(@Body userregistration: UserRegistration) : Response<LoginResponse>

    @FormUrlEncoded
    @POST("/User/login")
    suspend fun checkUser(@Field("username")username : String, @Field("password")password : String) :Response<LoginResponse>


}