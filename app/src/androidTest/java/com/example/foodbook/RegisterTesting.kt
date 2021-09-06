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
class RegisterTesting {
    @get:Rule
    val ruleTest = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerUI()
    {
        onView(withId(R.id.rgFullname))
            .perform(typeText("Krishnaaaaa"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.rgEmail))
            .perform(typeText("krishnaasdasd@gmail.com"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.rgUsername))
            .perform(typeText("krishsadsnaNeupane"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.rgPassword))
            .perform(typeText("krishsnapandey"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.rgCPassword))
            .perform(typeText("krishnapandey"))
        closeSoftKeyboard()
        Thread.sleep(400)

        onView(withId(R.id.btnSignup))
            .perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.rgUsername))
            .check(matches(isDisplayed()))
    }
}