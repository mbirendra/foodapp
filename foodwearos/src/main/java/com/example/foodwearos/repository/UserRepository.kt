package com.example.foodwearos.repository

import com.example.foodwearos.api.ApiRequest
import com.example.foodwearos.api.ServiceBuilder
import com.example.foodwearos.api.UserAPI
import com.example.foodwearos.response.FoodResponse
import com.example.foodwearos.response.LoginResponse

class UserRepository():ApiRequest() {
    var userAPI = ServiceBuilder.buildService(UserAPI::class.java)

    suspend fun loginUser(username:String,password:String):LoginResponse
    {
        return apiRequest {
            userAPI.checkUser(username,password)
        }
    }

    suspend fun getMyFood(): FoodResponse
    {
        return apiRequest {
            userAPI.showAllRecipe(ServiceBuilder.token!!)
        }
    }

}