package com.example.foodbook.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbook.R
import com.example.foodbook.adapter.FoodAdapter
import com.example.foodbook.api.ServiceBuilder
import com.example.foodbook.repository.FoodRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ExploreFragment : Fragment() {
    private lateinit var recycler:RecyclerView
    private lateinit var adapter: FoodAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        recycler = root.findViewById(R.id.recycler)
        ServiceBuilder.toggle = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = FoodRepository()
                val response = repo.getMyFood()
                if(response.success == true)
                {
                    var lstData = response.Foods
                    withContext(Dispatchers.Main)
                    {
                       adapter = FoodAdapter(requireContext(),lstData!!)
                        recycler.adapter = adapter
                        recycler.layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)

                    }
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(), "No Food", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            catch (ex:Exception)
            {
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(requireContext(), "${ex.printStackTrace()}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return root
    }
}