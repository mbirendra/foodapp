package com.example.foodbook

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class AddRecipeTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(DashboardActivity::class.java)

    @Test
    fun addRecipeUI()
    {
        runBlocking {
            var userRepository = UserRepository()
            ServiceBuilder.token = "Bearer "+userRepository.checkUser("Oscar","password").token
        }

        onView(withId(R.id.navigation_saved))
            .perform(click())

        onView(withId(R.id.etRecipeTitle))
            .perform(typeText("Buff MoMo"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etRecipeDesc))
            .perform(typeText("A mixture of buff ingredients wrapped in a dough."))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.rbVeg))
            .perform(click())
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.etPrepTime))
            .perform(typeText("2 hrs"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.btnAdd))
            .perform(scrollTo(),click())

        Thread.sleep(2000)

        onView(withId(R.id.etRecipeDesc))
            .check(matches(isDisplayed()))
    }
}