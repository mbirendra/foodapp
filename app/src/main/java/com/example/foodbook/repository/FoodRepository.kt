package com.example.foodbook.repository

import com.example.foodbook.api.MyApiRequest
import com.example.foodbook.api.FoodApi
import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.response.FoodResponse
import okhttp3.MultipartBody

class FoodRepository() : MyApiRequest() {
    private val FoodApi = ServiceBuilder.buildService(FoodApi::class.java)

//    suspend fun insertStudent(student: Food): AddStudentResponse {
//        return apiRequest {
//            studentApi.insertStudent(ServiceBuilder.token!!, student)
//        }
//    }

    suspend fun retrieveFood(type:String): FoodResponse
    {
        return apiRequest {
            FoodApi.retrieveFood(type)
        }
    }

    suspend fun insertFood(title:String,desc:String,category:String,type:String,preptime:String):FoodResponse
    {
        return apiRequest {
            FoodApi.addFood(ServiceBuilder.token!!,title,desc,category,type,preptime)
        }
    }

    suspend fun uploadFood(id:String,Foodimg:MultipartBody.Part):FoodResponse
    {
        return apiRequest {
            FoodApi.updateImg(id,Foodimg)
        }
    }

    suspend fun getMyFood():FoodResponse
    {
        return apiRequest {
            FoodApi.showAllFood(ServiceBuilder.token!!)
        }
    }

    suspend fun deleteFood(id:String):FoodResponse
    {
        return apiRequest {
            FoodApi.deleteFood(ServiceBuilder.token!!,id)
        }
    }

    suspend fun updateFoodDetails(title:String,desc:String,category:String,preptime:String,id:String):FoodResponse
    {
        return apiRequest {
            FoodApi.updateFoodDetails(ServiceBuilder.token!!,title,desc,category,preptime,id)
        }
    }

//    suspend fun uploadImage(id:String, body: MultipartBody.Part):ImageResponse{
//        return apiRequest {
//            studentApi.uploadImage(ServiceBuilder.token!!, id, body)
//        }
//    }
}