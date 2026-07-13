package com.app.smartcatalog.features.products.domain.usecase

import com.app.smartcatalog.core.error.AppError
import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.domain.model.Product
import com.app.smartcatalog.features.products.domain.model.ProductListResult
import com.app.smartcatalog.features.products.domain.repository.ProductRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchProductsUseCaseTest {

    @Test
    fun `blank query delegates to getProducts`() = runTest {
        val repository = FakeProductRepository()
        val useCase = SearchProductsUseCase(repository)

        val result = useCase("   ")

        assertTrue(result is Result.Success)
        assertEquals(1, repository.getProductsCalls)
        assertEquals(0, repository.searchProductsCalls)
    }

    @Test
    fun `non blank query calls searchProducts with trimmed query`() = runTest {
        val repository = FakeProductRepository()
        val useCase = SearchProductsUseCase(repository)

        val result = useCase("  phone  ")

        assertTrue(result is Result.Success)
        assertEquals(0, repository.getProductsCalls)
        assertEquals(1, repository.searchProductsCalls)
        assertEquals("phone", repository.lastSearchQuery)
        assertEquals(1, (result as Result.Success).data.products.size)
    }

    @Test
    fun `repository error is propagated`() = runTest {
        val repository = FakeProductRepository(shouldFailSearch = true)
        val useCase = SearchProductsUseCase(repository)

        val result = useCase("phone")

        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error is AppError.Network)
    }

    private class FakeProductRepository(
        private val shouldFailSearch: Boolean = false,
    ) : ProductRepository {
        var getProductsCalls = 0
        var searchProductsCalls = 0
        var lastSearchQuery: String? = null

        override suspend fun getProducts(limit: Int, skip: Int): Result<ProductListResult> {
            getProductsCalls++
            return Result.Success(
                ProductListResult(
                    products = listOf(sampleProduct(id = 1)),
                    total = 1,
                ),
            )
        }

        override suspend fun getProductById(id: Int): Result<Product> {
            return Result.Success(sampleProduct(id))
        }

        override suspend fun searchProducts(query: String): Result<ProductListResult> {
            searchProductsCalls++
            lastSearchQuery = query
            if (shouldFailSearch) {
                return Result.Error(AppError.Network("Search failed"))
            }
            return Result.Success(
                ProductListResult(
                    products = listOf(sampleProduct(id = 101, title = "Phone")),
                    total = 1,
                ),
            )
        }

        override suspend fun getCategories(): Result<List<com.app.smartcatalog.features.products.domain.model.Category>> {
            return Result.Success(emptyList())
        }

        override suspend fun getProductsByCategory(category: String): Result<ProductListResult> {
            return Result.Success(ProductListResult(emptyList(), 0))
        }

        private fun sampleProduct(id: Int, title: String = "Product $id") = Product(
            id = id,
            title = title,
            description = "Description",
            category = "electronics",
            price = 99.99,
            rating = 4.5,
            brand = "Brand",
            thumbnail = "https://example.com/image.jpg",
            images = emptyList(),
        )
    }
}
