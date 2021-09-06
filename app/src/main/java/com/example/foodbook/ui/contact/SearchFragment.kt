package com.example.foodbook.ui.contact

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodbook.Logout
import com.example.foodbook.MapsActivity
import com.example.foodbook.R

class SearchFragment : Fragment() {


    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var tvfullname : TextView
    private lateinit var tvEmail : TextView
    private lateinit var tvusername : TextView
    private lateinit var buttonLocation : TextView
    private lateinit var signoutbtn : TextView



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {



        searchViewModel =
                ViewModelProvider(this).get(SearchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        searchViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        tvfullname = root.findViewById(R.id.tvfullname)
        tvEmail = root.findViewById(R.id.tvEmail)
        tvusername = root.findViewById(R.id.tvusername)
        buttonLocation = root.findViewById(R.id.buttonLocation)
        signoutbtn = root.findViewById(R.id.signoutbtn)

        buttonLocation.setOnClickListener {
            startActivity(
                Intent(
                   requireContext(),
                    MapsActivity::class.java
                )
            )
        }

        signoutbtn.setOnClickListener {
            Logout(requireActivity(),requireContext()).logout()
        }
        return root
    }

}