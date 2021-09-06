package com.example.foodbook

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
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
class FetchMyRecipeTesting {
    @get:Rule
    val testRule = ActivityScenarioRule(DashboardActivity::class.java)

    @Test
    fun checkRecipeUI()
    {
        runBlocking {
            var userRepository = UserRepository()
            ServiceBuilder.token = "Bearer "+userRepository.checkUser("Oscar","password").token
        }

        Espresso.onView(ViewMatchers.withId(R.id.navigation_explore))
            .perform(ViewActions.click())

        Thread.sleep(400)

        Espresso.onView(ViewMatchers.withId(R.id.recycler))
            .check(matches(isDisplayed()))
    }
}