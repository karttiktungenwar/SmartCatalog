package com.app.smartcatalog.features.products.domain.usecase

import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.domain.model.ProductListResult
import com.app.smartcatalog.features.products.domain.repository.ProductRepository

class GetProductsUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(limit: Int = 30, skip: Int = 0): Result<ProductListResult> {
        return repository.getProducts(limit, skip)
    }
}
