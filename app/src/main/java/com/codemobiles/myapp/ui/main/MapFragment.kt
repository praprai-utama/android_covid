package com.codemobiles.myapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codemobiles.myapp.databinding.FragmentMapBinding
import com.codemobiles.myapp.MapsActivity


class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)

        binding.googlemapButton.setOnClickListener {
            startActivity(Intent(context, MapsActivity::class.java))
        }

        return binding.root
    }
}