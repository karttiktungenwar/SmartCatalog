package com.app.smartcatalog.features.products.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponseDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)

@Serializable
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val brand: String? = null,
    val thumbnail: String,
    val images: List<String> = emptyList(),
)

@Serializable
data class CategoryDto(
    val slug: String,
    val name: String,
    val url: String,
)
