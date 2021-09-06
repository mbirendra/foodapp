package com.example.foodbook

import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.foodbook.notification.NotificationSender
import com.example.foodbook.ui.home.HomeFragment
import com.example.foodbook.ui.saved.SaveddFragment

class DashboardActivity : AppCompatActivity(), SensorEventListener {
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView: BottomNavigationView
    lateinit var toggle: ActionBarDrawerToggle
    var fragId:MutableList<Int> = mutableListOf( R.id.navigation_home, R.id.navigation_explore, R.id.navigation_saved,R.id.navigation_search)


    private lateinit var sensorManager: SensorManager
    var sensor: Sensor? = null
    var sensor2: Sensor? = null
    var sensor3: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        navView= findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_search, R.id.navigation_explore,R.id.navigation_saved))
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        drawerLayout = findViewById(R.id.drawerlayout)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if(!checkSensor()) {
            return
        }
        else
        {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensor3 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this,sensor2, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this,sensor3, SensorManager.SENSOR_DELAY_NORMAL)
        }


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.contact -> {
                    Toast.makeText(applicationContext,
                        "Contact Us is clicked",
                        Toast.LENGTH_LONG).show()
                    true
                }
                R.id.help -> {Toast.makeText(applicationContext, "Help is clicked", Toast.LENGTH_LONG)
                    .show()
                    true
                }
                R.id.miItem3 -> {Toast.makeText(applicationContext, "Share 3 is clicked", Toast.LENGTH_LONG)
                    .show()
                    true
                }
                R.id.miItem4 -> {Toast.makeText(applicationContext,
                    "Rate us 4 is clicked",
                    Toast.LENGTH_LONG).show()
                    true
                }
                R.id.miItem5 -> {Toast.makeText(applicationContext, "Setting is clicked", Toast.LENGTH_LONG)
                    .show()
                    true
                }
                else -> {
                    false
                }

            }

        }

        //check for permission
        if (!hasPermission()){
            requestPermission()
        }



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 1)
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

    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED)
            {
                hasPermission = false
            }
        }
        return hasPermission
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if(event!!.sensor.type == Sensor.TYPE_LIGHT)
        {
            val values = event!!.values[0]
            if(values > 40)
            {
                NotificationSender(this,"Excessive brightness can be harmful to your eyes !","").createHighPriority()
            }
        }

        if (event!!.sensor.type == Sensor.TYPE_PROXIMITY) {
            val values = event.values[0]
            if (values <= 4) {
                val logOut = Logout(this, this)
                logOut.logout()
            }
        }
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val values = event.values
            val xAxis = values[0]

            if (xAxis < (-7)) {

                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.nav_host_fragment, SaveddFragment())
                    commit()
                    navView.menu.findItem(fragId[2]).isChecked = true
                }
            }
            if (xAxis > (7)) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.nav_host_fragment, HomeFragment())
                    commit()
                }


                navView.menu.findItem(fragId[0]).isChecked = true
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}