package com.app.smartcatalog.features.products.domain.repository

import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.domain.model.Category
import com.app.smartcatalog.features.products.domain.model.Product
import com.app.smartcatalog.features.products.domain.model.ProductListResult

interface ProductRepository {
    suspend fun getProducts(limit: Int = 30, skip: Int = 0): Result<ProductListResult>
    suspend fun getProductById(id: Int): Result<Product>
    suspend fun searchProducts(query: String): Result<ProductListResult>
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getProductsByCategory(category: String): Result<ProductListResult>
}
