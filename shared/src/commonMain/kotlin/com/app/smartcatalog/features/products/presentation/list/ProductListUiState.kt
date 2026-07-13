package com.app.smartcatalog.features.products.presentation.list

import com.app.smartcatalog.features.products.domain.model.Category
import com.app.smartcatalog.features.products.domain.model.Product

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val totalProducts: Int = 0,
)
