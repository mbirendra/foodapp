package com.example.foodbook

import android.app.Activity
import android.content.Context
import android.content.Intent

class Logout(val activity: Activity, val context: Context) {
    fun logout()
    {
        var pref = activity.getSharedPreferences("MainPref",Context.MODE_PRIVATE)
        var editor = pref.edit()
        editor.putString("username","")
        editor.putString("password","")
        editor.apply()
        val intent = Intent(context,LoginActivity::class.java)
        context.startActivity(intent)
        activity.finish()
    }
}