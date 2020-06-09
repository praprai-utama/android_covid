package com.codemobiles.myapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codemobiles.myapp.GridSpacingItemDecoration
import com.codemobiles.myapp.databinding.CustomStockListBinding
import com.codemobiles.myapp.databinding.FragmentStockBinding


class StockFragment : Fragment() {

    private lateinit var customAdapter: CustomStockListAdapter
    private lateinit var binding: FragmentStockBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockBinding.inflate(layoutInflater)

        customAdapter = CustomStockListAdapter(listOf("123", "21412", "12412", "124124"))

        binding.stockRecyclerview.adapter = customAdapter

        // important!!!!!!!!
        binding.stockRecyclerview.layoutManager = GridLayoutManager(context, 2)

        // optional
        binding.stockRecyclerview.addItemDecoration(GridSpacingItemDecoration(2, 20, true))


        // recommended
        binding.stockRecyclerview.setHasFixedSize(true)

        return binding.root
    }

    // primary class
    inner class CustomStockListAdapter(private var productList: List<String>) :
        RecyclerView.Adapter<CustomStockListAdapter.ViewHolder>() {

        // primary class
        inner class ViewHolder(view: View, val binding: CustomStockListBinding) :
            RecyclerView.ViewHolder(view) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                CustomStockListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding.root, binding)
        }

        override fun getItemCount() = productList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val binding = holder.binding
            binding.productNameTextview.text = "iBlurBlur"
        }

    }

}