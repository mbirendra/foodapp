package com.example.foodbook.repository

import com.example.foodbook.api.MyApiRequest
import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.api.UserApi
import com.example.foodbook.entity.UserRegistration
import com.example.foodbook.response.LoginResponse

class UserRepository : MyApiRequest() {
    val userApi = ServiceBuilder.buildService(UserApi::class.java)

    suspend fun registerUser(userregistration: UserRegistration): LoginResponse {
        return apiRequest {
            userApi.registerUser(userregistration)
        }
    }

    suspend fun  checkUser(username : String, password : String) : LoginResponse {
        return apiRequest {
            userApi.checkUser(username,password)
        }
    }

}