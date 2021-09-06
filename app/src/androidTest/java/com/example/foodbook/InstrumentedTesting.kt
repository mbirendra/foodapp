package com.example.foodbook

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class InstrumentedTesting {

    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testLoginUI(){
        onView(withId(R.id.lgUsername))
            .perform(typeText("Oscar"))
        closeSoftKeyboard()
        Thread.sleep(1000)

        onView(withId(R.id.lgPassword))
            .perform(typeText("password"))

        closeSoftKeyboard()
        Thread.sleep(1000)

        onView(withId(R.id.btnLogin))
            .perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.nav_view))
            .check(matches(isDisplayed()))


    }
}