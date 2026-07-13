package com.app.smartcatalog.features.products.domain.usecase

import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.domain.model.ProductListResult
import com.app.smartcatalog.features.products.domain.repository.ProductRepository

class SearchProductsUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(query: String): Result<ProductListResult> {
        if (query.isBlank()) {
            return repository.getProducts()
        }
        return repository.searchProducts(query.trim())
    }
}
