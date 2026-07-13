package com.app.smartcatalog.features.products.domain.usecase

import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.domain.model.Product
import com.app.smartcatalog.features.products.domain.repository.ProductRepository

class GetProductByIdUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(id: Int): Result<Product> {
        return repository.getProductById(id)
    }
}
