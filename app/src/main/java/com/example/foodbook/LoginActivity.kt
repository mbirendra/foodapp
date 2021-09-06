package com.example.foodbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.notification.NotificationSender
import com.example.foodbook.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var lgUsername: EditText
    private lateinit var lgPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var signup: TextView
    private lateinit var linearLayout: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding()
        listener()

    }


    private fun listener() {
        btnLogin.setOnClickListener(this)
    }

    private fun binding() {
        lgUsername = findViewById(R.id.lgUsername)
        lgPassword = findViewById(R.id.lgPassword)
        signup = findViewById(R.id.signup)
        btnLogin = findViewById(R.id.btnLogin)
        linearLayout = findViewById(R.id.linearLayout)
    }

    fun SignupLink(view: View) {
        if (view.id == signup.id) {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onClick(v: View?) {
        val username = lgUsername.text.toString()
        val password = lgPassword.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepository()
                val response = repository.checkUser(username, password)
                if(response.success == true)
                {
                    NotificationSender(this@LoginActivity,"Logged in Successfully","").createHighPriority()
                    saveSharedPref()
                    ServiceBuilder.token ="Bearer "+response.token
                    val intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    withContext(Dispatchers.Main){
                      val snk= Snackbar.make(linearLayout,"${response.message}",Snackbar.LENGTH_INDEFINITE)
                       snk.setAction("Ok",View.OnClickListener {
                           snk.dismiss()
                       })
                       snk.show()

                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoginActivity, "login error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun saveSharedPref() {
        val sharedPref = getSharedPreferences("MainPref", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", lgUsername.text.toString())
        editor.putString("password", lgPassword.text.toString())
        editor.apply()
    }
}