package com.example.foodbook

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbook.adapter.FoodAdapter
import com.example.foodbook.db.FoodDB
import com.example.foodbook.entity.Food
import com.example.foodbook.repository.FoodRepository
import kotlinx.coroutines.*

class FoodViewActivity : AppCompatActivity() {
    private var lstFood: List<Food> = listOf()
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_view)

        recyclerView = findViewById(R.id.FoodView)

        runBlocking {
            emptyTable().await()

            getdata().await()


            loadRoomDatabase().await()

        }
        val adapter = FoodAdapter(this@FoodViewActivity, lstFood!!.toMutableList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this@FoodViewActivity,2,GridLayoutManager.VERTICAL,false)
    }

    fun emptyTable() = GlobalScope.async {
        FoodDB.getInstance(this@FoodViewActivity).getFoodDAO().deleteAll()


        Log.i("deleted", "Food table")


    }

    fun getdata() = GlobalScope.async {
        try {
            FoodDB.getInstance(this@FoodViewActivity).getFoodDAO().deleteAll()
            val repository = FoodRepository()
            val type = intent.getStringExtra("type")

            val response = repository.retrieveFood(type!!)
            if (response.success == true)
                response.Foods!!.forEach {
                    FoodDB.getInstance(this@FoodViewActivity).getFoodDAO()
                        .insertFood(it)
                    Log.i("inserted", it.Foodtitle.toString())
                }


        } catch (ex: Exception) {

            println(ex.printStackTrace())

//                withContext(Dispatchers.Main){
//                    Toast.makeText(this@LoginActivity, "login error", Toast.LENGTH_SHORT).show()
//                }
        }
    }

    fun loadRoomDatabase() = GlobalScope.async {
        Log.i("loaded", "Food room")
        runBlocking {
            lstFood = FoodDB.getInstance(this@FoodViewActivity).getFoodDAO().findAll()
        }


    }


}
