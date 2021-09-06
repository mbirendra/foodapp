package com.example.foodwearos

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.TextView
import android.widget.Toast

import com.example.foodwearos.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DashboardActivity : WearableActivity() {
    private lateinit var tvFood:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        tvFood = findViewById(R.id.tvFood)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo =UserRepository()
                val response = repo.getMyFood()
                if(response.success == true)
                {
                    withContext(Dispatchers.Main)
                    {

                        tvFood.text = "${response.foods!!.size} Food found"
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {

                        tvFood.text = "No Food found"
                    }

                }
            }
            catch (ex: Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(this@DashboardActivity, "${ex.printStackTrace()}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // Enables Always-on
        setAmbientEnabled()
    }
}