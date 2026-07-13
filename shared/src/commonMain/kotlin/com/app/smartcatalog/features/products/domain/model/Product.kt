package com.app.smartcatalog.features.products.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val brand: String,
    val thumbnail: String,
    val images: List<String>,
)

data class Category(
    val slug: String,
    val name: String,
)

data class ProductListResult(
    val products: List<Product>,
    val total: Int,
)
