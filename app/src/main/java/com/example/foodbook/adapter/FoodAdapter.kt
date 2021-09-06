package com.example.foodbook.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbook.R
import com.example.foodbook.FoodView
import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.entity.Food

class FoodAdapter(val context: Context, val lstFood: MutableList<Food>) :
    RecyclerView.Adapter<FoodAdapter.StudentViewHolder>() {
    class StudentViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvFoodtitle: TextView
        var tvFoodImage: ImageView
        var tvFooddesc: TextView
        var tvFoodcategory: TextView
        var tvprepTime : TextView
        var layoutConstraint : ConstraintLayout

        init {

            tvFoodtitle = v.findViewById(R.id.tvFoodtitle)
            tvFoodImage = v.findViewById(R.id.imgFood)
            tvFooddesc = v.findViewById(R.id.tvFooddescription)
            tvFoodcategory = v.findViewById(R.id.tvFoodcategory)
            tvprepTime = v.findViewById(R.id.tvprepTime)
            layoutConstraint = v.findViewById(R.id.layoutConstraint)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.food_layout, parent, false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lstFood.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val FoodData = lstFood[position]
        holder.tvFoodtitle.text = FoodData.Foodtitle //FROM THE Food.kt class
//        holder.tvFooddesc.text = FoodData.Fooddesc (if you don't want to show this)
        holder.tvFoodcategory.text = FoodData.foodcategory
        holder.tvprepTime.text = FoodData.preptime
//
        val imagePath = ServiceBuilder.loadImagePath() + FoodData.foodimg!!.replace("\\","/")

        Glide.with(context)
            .load(imagePath)
            .into(holder.tvFoodImage)
                    print(imagePath)

        holder.layoutConstraint.setOnClickListener {
            var intent = Intent(context,FoodView::class.java)
            intent.putExtra("Food",FoodData)
            context.startActivity(intent)
        }



        }
    }


