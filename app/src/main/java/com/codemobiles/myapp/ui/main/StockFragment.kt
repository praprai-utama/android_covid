package com.codemobiles.myapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codemobiles.myapp.GridSpacingItemDecoration
import com.codemobiles.myapp.R
import com.codemobiles.myapp.databinding.CustomStockListBinding
import com.codemobiles.myapp.databinding.FragmentStockBinding
import com.codemobiles.myapp.models.ProductResponse
import com.codemobiles.myapp.services.APIClient
import com.codemobiles.myapp.services.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StockFragment : Fragment() {

    private lateinit var customAdapter: CustomStockListAdapter
    private lateinit var binding: FragmentStockBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockBinding.inflate(layoutInflater)

        customAdapter = CustomStockListAdapter(null)

        binding.stockRecyclerview.apply {
            adapter = customAdapter
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(GridSpacingItemDecoration(2, 20, true))
            setHasFixedSize(true)
        }

        binding.refresh.setOnRefreshListener {
            feedNetwork()
        }


//        binding.stockRecyclerview.adapter = customAdapter
//
//        // important!!!!!!!!
//        binding.stockRecyclerview.layoutManager = GridLayoutManager(context, 2)
//
//        // optional
//        binding.stockRecyclerview.addItemDecoration(GridSpacingItemDecoration(2, 20, true))
//
//        // recommended
//        binding.stockRecyclerview.setHasFixedSize(true)

        feedNetwork()

        return binding.root
    }

    private fun feedNetwork() {
        val call: Call<ProductResponse> =
            APIClient.getClient().create(APIService::class.java).getProducts()

        Log.d("cm_network", call.request().url().toString())

        // object expression
        call.enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                binding.refresh.isRefreshing = false

                Log.e("cm_network", t.message)
            }

            override fun onResponse( call: Call<ProductResponse>,response: Response<ProductResponse> ) {
                if (response.isSuccessful) {
//                    customAdapter.productList = response.body()
////                    // important !!!
////                    customAdapter.notifyDataSetChanged()
                    customAdapter.apply {
                        // this
                        productList = response.body()
                    }.run {
                        // important !!!
                        notifyDataSetChanged()
                    }
                } else {
                    Log.d("cm_network", "nok")
                }

                binding.refresh.isRefreshing = false
            }
        })
    }

    // primary class
    inner class CustomStockListAdapter(var productList: ProductResponse?) :
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

        override fun getItemCount() = if (productList == null) 0 else productList!!.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            productList?.let { products ->
                // scope function (kotlin)
                val product = products[position]

                val binding = holder.binding
                binding.productNameTextview.text = product.name
                binding.productPriceTextview.text = "$ " + product.price.toString()
                binding.productStockTextview.text = product.stock.toString() + " price"

                binding.productDetailTextview.text = "CodeMobiles Workshop"

                Glide.with(binding.productImageview.context)
                    .applyDefaultRequestOptions(RequestOptions().centerCrop())
                    .load(APIClient.getImageURL() + product.image)
                    .error(R.drawable.logo)
                    .into(binding.productImageview)
            }
        }

    }

}