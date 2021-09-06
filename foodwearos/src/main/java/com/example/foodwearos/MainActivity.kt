package com.example.foodwearos

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.foodwearos.api.ServiceBuilder

import com.example.foodwearos.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : WearableActivity(),View.OnClickListener {
    private lateinit var etUsername:EditText
    private lateinit var etPassword:EditText
    private lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener(this)
        // Enables Always-on
        setAmbientEnabled()
    }

    private fun loginUser()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = UserRepository()
                val response = repo.loginUser(etUsername.text.toString(),etPassword.text.toString())
                if(response.success == true)
                {
                    ServiceBuilder.token = "Bearer "+response.token
                    withContext(Dispatchers.Main)
                    {

                        var intent = Intent(this@MainActivity,DashboardActivity::class.java)
                        startActivity(intent)
                    }
                }
                else
                {

                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    etUsername.snackbar("${ex.toString()}")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnLogin ->{
                loginUser()
            }
        }
    }
}