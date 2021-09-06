package com.example.foodbook

import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.entity.UserRegistration
import com.example.foodbook.repository.FoodRepository
import com.example.foodbook.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class AppTesting {
    @Test
    fun loginTesting()
    {
        runBlocking {
            var expectedResult = true
            var repo = UserRepository()
            var response = repo.checkUser("test1","123456")
            var actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }

    }

    @Test
    fun registerTesting()
    {
        runBlocking {
            var expectedResult = true
            var user = UserRegistration(fullname="test1",email="test1@gmail.com",username="test1",password ="123456")
            var repo = UserRepository()
            var response = repo.registerUser(user)
            var actualResult=response.success
            Assert.assertEquals(expectedResult,actualResult)

        }
    }

    @Test
    fun addRecipe()
    {
        runBlocking {
            var expectedResult = true
            var user = UserRepository()
            ServiceBuilder.token = "Bearer "+user.checkUser("Oscar","password").token
            var repo = FoodRepository()
            var response = repo.insertFood("Banana","Yummy","Veg","__","1 min")
            var actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)

        }
    }

    @Test
    fun updateRecipe()
    {
        runBlocking {
            var expectedResult = true
            var user = UserRepository()
            ServiceBuilder.token = "Bearer "+user.checkUser("Oscar","password").token
            var repo = FoodRepository()
            var response = repo.updateFoodDetails("Banana","Yummy","Veg","__","6075d31a9cc9b432bce4339a")
            var actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)

        }
    }

    @Test
    fun deleteRecipe()
    {
        runBlocking {
            var expectedResult = true
            var user = UserRepository()
            ServiceBuilder.token = "Bearer "+user.checkUser("Oscar","password").token
            var repo = FoodRepository()
            var response = repo.deleteFood("6075d31a9cc9b432bce4339a")
            var actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

    @Test
    fun loginTestings()
    {
        runBlocking {
            var expectedResult = false
            var repo = UserRepository()
            var response = repo.checkUser("Oscar","password")
            var actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }

    }

    @Test
    fun registerTestings()
    {
        runBlocking {
            var expectedResult = true
            var user = UserRegistration(fullname="Krishna",email="krishna@gmail.com",username="Krishna888",password ="Krishna888")
            var repo = UserRepository()
            var response = repo.registerUser(user)
            var actualResult=response.success
            Assert.assertEquals(expectedResult,actualResult)

        }
    }

    @Test
    fun addRecipes()
    {
        runBlocking {
            var expectedResult = false
            var user = UserRepository()
            ServiceBuilder.token = "Bearer "+user.checkUser("Oscar","password").token
            var repo = FoodRepository()
            var response = repo.insertFood("Banana","Yummy","Veg","__","1 min")
            var actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)

        }
    }

    @Test
    fun updateRecipes()
    {
        runBlocking {
            var expectedResult = false
            var user = UserRepository()
            ServiceBuilder.token = "Bearer "+user.checkUser("Oscar","password").token
            var repo = FoodRepository()
            var response = repo.updateFoodDetails("Banana","Yummy","Veg","__","6075d31a9cc9b432bce4339a")
            var actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)

        }
    }

    @Test
    fun deleteRecipes()
    {
        runBlocking {
            var expectedResult = false
            var user = UserRepository()
            ServiceBuilder.token = "Bearer "+user.checkUser("Oscar","password").token
            var repo = FoodRepository()
            var response = repo.deleteFood("6075d31a9cc9b432bce4339a")
            var actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }
}