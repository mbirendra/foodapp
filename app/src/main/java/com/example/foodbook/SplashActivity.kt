package com.example.foodbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.repository.UserRepository
import kotlinx.coroutines.*
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var myPref = getSharedPreferences("MainPref", Context.MODE_PRIVATE)
        var username = myPref.getString("username","")
        var password = myPref.getString("password","")
        if(username!="" && password!="")
        {
            CoroutineScope(Dispatchers.IO).launch {

                delay(1000)
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.checkUser(username!!,password!!)
                    if(response.success == true)
                    {
                        ServiceBuilder.token="Bearer "+response.token
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                DashboardActivity::class.java
                            )
                        )
                        finish()
                    }
                    else
                    {
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                LoginActivity::class.java
                            )
                        )
                        finish()
                    }
                }
                catch (e: Exception)
                {

                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(this@SplashActivity, "${e.toString()}", Toast.LENGTH_SHORT).show()

                    }
                }

            }
        }
        else
        {
            startActivity(
                Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
            )
        }
    }
}