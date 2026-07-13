package com.app.smartcatalog.features.products.data.remote

import com.app.smartcatalog.core.network.ApiConstants
import com.app.smartcatalog.features.products.data.dto.CategoryDto
import com.app.smartcatalog.features.products.data.dto.ProductDto
import com.app.smartcatalog.features.products.data.dto.ProductsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductApi(
    private val httpClient: HttpClient,
) {
    suspend fun getProducts(limit: Int = 30, skip: Int = 0): ProductsResponseDto {
        return httpClient.get(ApiConstants.PRODUCTS_PATH) {
            parameter("limit", limit)
            parameter("skip", skip)
        }.body()
    }

    suspend fun getProductById(id: Int): ProductDto {
        return httpClient.get("${ApiConstants.PRODUCTS_PATH}/$id").body()
    }

    suspend fun searchProducts(query: String): ProductsResponseDto {
        return httpClient.get(ApiConstants.PRODUCTS_SEARCH_PATH) {
            parameter("q", query)
        }.body()
    }

    suspend fun getCategories(): List<CategoryDto> {
        return httpClient.get(ApiConstants.PRODUCTS_CATEGORIES_PATH).body()
    }

    suspend fun getProductsByCategory(category: String): ProductsResponseDto {
        return httpClient.get("${ApiConstants.PRODUCT_CATEGORY_PATH}/$category").body()
    }
}
