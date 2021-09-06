package com.example.foodbook

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.foodbook.entity.UserRegistration
import com.example.foodbook.notification.NotificationSender
import com.example.foodbook.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity(), View.OnClickListener, SensorEventListener {
    private lateinit var rgFullname: EditText
    private lateinit var rgEmail: EditText
    private lateinit var rgUsername: EditText
    private lateinit var rgPassword: EditText
    private lateinit var rgCPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var login: TextView
    private lateinit var sensorManager: SensorManager
    var sensor:Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(!checkSensor()) {
            return
        }
        else
        {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        listener()

    }



    private fun checkSensor():Boolean
    {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null)
        {
            flag = false
        }
        return flag
    }





    private fun listener() {
        btnSignup.setOnClickListener(this)
    }


    private fun binding() {
        rgFullname = findViewById(R.id.rgFullname)
        rgEmail = findViewById(R.id.rgEmail)
        rgUsername = findViewById(R.id.rgUsername)
        rgPassword = findViewById(R.id.rgPassword)
        rgCPassword = findViewById(R.id.rgCPassword)
        btnSignup = findViewById(R.id.btnSignup)
        login = findViewById(R.id.login)
    }

    fun loginLink(view: View) {
        if (view.id == login.id) {
            startActivity(Intent(this, login::class.java))
        }
    }

    override fun onClick(v: View?) {
        val fullname = rgFullname.text.toString()
        val email = rgEmail.text.toString()
        val username = rgUsername.text.toString()
        val password = rgPassword.text.toString()
        val confirmPassword = rgCPassword.text.toString()

        if (validation()) {
            if (password != confirmPassword) {
                rgCPassword.error = "Password does not match"
                rgCPassword.requestFocus()
                return
            } else {
                val userregistration = UserRegistration(fullname = fullname, email = email, username = username, password = password)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val userRepository = UserRepository()
                        val response = userRepository.registerUser(userregistration)
                        if(response.success == true){
                            withContext(Main) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "${response.message}", Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
                                startActivity(intent)
                                clear()
                            }
                            val notificationChannel = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                NotificationChannel("Food Nepal","My notification",NotificationManager.IMPORTANCE_DEFAULT)
                            } else {
                                TODO("VERSION.SDK_INT < O")
                            }
                            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.createNotificationChannel(notificationChannel)
                            val notificationInstance = NotificationCompat.Builder(this@RegisterActivity,"Food Nepal")
                                .setSmallIcon(android.R.drawable.arrow_up_float)
                                .setContentTitle("Food Nepal")
                                .setContentText("Welcome to Food Nepal ${fullname}!!")
                                .setAutoCancel(true)
                            notificationManager.notify(1,notificationInstance.build())
                        }
                    } catch (ex: Exception) {
                        withContext(Main) {
                            Toast.makeText(
                                this@RegisterActivity,
                                ex.toString(), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
//                    UserDB.getInstance(this@register).getUserDAO().registerUser(user)
//
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(this@register, "Account registered successfully :) ", Toast.LENGTH_SHORT).show()
//                        clear()
                    }
                }

            }
        }

    private fun clear() {
        rgFullname.text.clear()
        rgUsername.text.clear()
        rgEmail.text.clear()
        rgPassword.text.clear()
        rgCPassword.text.clear()
    }

    private fun validation(): Boolean {
        if (TextUtils.isEmpty(rgFullname.text)) {
            rgFullname.error = "Fullname required !"
            rgFullname.requestFocus()
            return false
        } else if (TextUtils.isEmpty(rgEmail.text)) {
            rgEmail.error = "Email required !"
            rgEmail.requestFocus()
            return false
        } else if (TextUtils.isEmpty(rgUsername.text)) {
            rgUsername.error = "Username required !"
            rgUsername.requestFocus()
            return false
        } else if (TextUtils.isEmpty(rgPassword.text)) {
            rgPassword.error = "Password must not be empty !"
            rgPassword.requestFocus()
            return false
        } else if (TextUtils.isEmpty(rgCPassword.text)) {
            rgCPassword.error = "Confirm your password !"
            rgCPassword.requestFocus()
            return false
        }
        return true

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        if(values > 20000)
        {
            println(values)
            NotificationSender(this,"Excessive brightness is harmful to your eyes","").createHighPriority()
        }




    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}
