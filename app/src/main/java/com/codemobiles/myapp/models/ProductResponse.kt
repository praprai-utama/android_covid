package com.codemobiles.myapp.models

class ProductResponse : ArrayList<ProductResponseItem>()

data class ProductResponseItem(
    val createdAt: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val stock: Int,
    val updatedAt: String
)