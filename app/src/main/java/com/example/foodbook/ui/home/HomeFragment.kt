package com.example.foodbook.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodbook.R
import com.example.foodbook.FoodViewActivity
import com.example.foodbook.api.ServiceBuilder

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeCarousel : ImageSlider
    private lateinit var breakfast : ImageView
    private lateinit var lunch : ImageView
    private lateinit var dinner : ImageView
    private lateinit var bakery : ImageView



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        ServiceBuilder.toggle = false

        homeCarousel = root.findViewById(R.id.homeCarousel)
        breakfast = root.findViewById(R.id.breakfast)
        lunch = root.findViewById(R.id.lunch)
        dinner = root.findViewById(R.id.dinner)
        bakery = root.findViewById(R.id.bakery)

        initializeCarousel()


        breakfast.setOnClickListener{
            val intent = Intent(activity, FoodViewActivity::class.java)
            intent.putExtra("type","Breakfast")
            startActivity(intent)
        }
        lunch.setOnClickListener{
            val intent = Intent(activity, FoodViewActivity::class.java)
            intent.putExtra("type","Lunch")
            startActivity(intent)
        }
        dinner.setOnClickListener{
            val intent = Intent(activity, FoodViewActivity::class.java)
            intent.putExtra("type","Dinner")
            startActivity(intent)
        }
        bakery.setOnClickListener{
            val intent = Intent(activity, FoodViewActivity::class.java)
            intent.putExtra("type","Bakery")
            startActivity(intent)
        }

        return root
    }


    private fun initializeCarousel()
    {
        var sliders : MutableList<SlideModel> = mutableListOf(
            SlideModel("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2015/11/20/3/FNK_Kids-Can-Make-OPENER_s4x3.jpg.rend.hgtvcom.616.462.suffix/1448050653882.jpeg","Quick Breakfast",
            ),
            SlideModel("https://hips.hearstapps.com/del.h-cdn.co/assets/17/31/1501791674-delish-chicken-curry-horizontal.jpg","Indian Dishes Recipe"),
            SlideModel("https://144f2a3a2f948f23fc61-ca525f0a2beaec3e91ca498facd51f15.ssl.cf3.rackcdn.com/uploads/food_portal_data/recipes/recipe/hero_article_image/964/8d5f4f646c43d84d2d33/letterbox_Teriyaki_20salmon_20noodles.jpg","Baking Recipe"),
            SlideModel("https://images.immediate.co.uk/production/volatile/sites/2/2018/05/Jaffa-cupcakes-0228cdb.jpg?quality=90&crop=18px%2C4249px%2C6155px%2C2648px&resize=960%2C408","Bakery Recipe")
        )
        homeCarousel.setImageList(sliders, ScaleTypes.FIT)
    }

}