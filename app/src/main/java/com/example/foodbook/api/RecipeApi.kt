package com.example.foodbook.api

import com.example.foodbook.response.FoodResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface FoodApi {
    //retrieve
    @GET("Food/allrec/{type}")
    suspend fun retrieveFood(

        @Path("type") type:String
    ):Response<FoodResponse>

    @FormUrlEncoded
    @POST("Food/insertFood")
    suspend fun addFood(
        @Header("Authorization") token:String,
        @Field("Foodtitle") title:String,
        @Field("Fooddesc") desc:String,
        @Field("foodcategory") foodCategory:String,
        @Field("foodtype") foodType:String,
        @Field("preptime") prep :String
    ):Response<FoodResponse>

    @Multipart
    @PUT("uploadFoodImg/{id}")
    suspend fun updateImg(
        @Path("id") id:String,
        @Part Foodimg:MultipartBody.Part
    ):Response<FoodResponse>

    @GET("Food/allrec")
    suspend fun showAllFood(
        @Header("Authorization") token:String
    ):Response<FoodResponse>

    @DELETE("Food/delete/{id}")
    suspend fun deleteFood(
        @Header("Authorization") token:String,
        @Path("id") id:String
    ):Response<FoodResponse>

    @FormUrlEncoded
    @PUT("update/Food")
    suspend fun updateFoodDetails(
        @Header("Authorization") token:String,
        @Field("Foodtitle") title:String,
        @Field("Fooddesc") desc:String,
        @Field("foodcategory") foodCategory:String,
        @Field("preptime") preprime:String,
        @Field("id") id :String
    ):Response<FoodResponse>
}