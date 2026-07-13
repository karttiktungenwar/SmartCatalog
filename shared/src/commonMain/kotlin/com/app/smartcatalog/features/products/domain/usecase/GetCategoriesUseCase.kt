package com.app.smartcatalog.features.products.domain.usecase

import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.domain.model.Category
import com.app.smartcatalog.features.products.domain.model.ProductListResult
import com.app.smartcatalog.features.products.domain.repository.ProductRepository

class GetCategoriesUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return repository.getCategories()
    }
}

class GetProductsByCategoryUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(category: String): Result<ProductListResult> {
        return repository.getProductsByCategory(category)
    }
}
