package com.app.smartcatalog.features.products.presentation.detail

import com.app.smartcatalog.features.products.domain.model.Product

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
