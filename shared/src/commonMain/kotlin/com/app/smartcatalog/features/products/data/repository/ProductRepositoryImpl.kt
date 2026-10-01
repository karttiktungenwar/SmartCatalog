package com.app.smartcatalog.features.products.data.repository

import com.app.smartcatalog.core.cache.InMemoryCache
import com.app.smartcatalog.core.error.AppError
import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.data.mapper.toDomain
import com.app.smartcatalog.features.products.data.remote.ProductApi
import com.app.smartcatalog.features.products.domain.model.Category
import com.app.smartcatalog.features.products.domain.model.Product
import com.app.smartcatalog.features.products.domain.model.ProductListResult
import com.app.smartcatalog.features.products.domain.repository.ProductRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

class ProductRepositoryImpl(
    private val productApi: ProductApi,
    private val productCache: InMemoryCache<Int, Product>,
    private val categoryCache: InMemoryCache<String, List<Category>>,
) : ProductRepository {

    override suspend fun getProducts(limit: Int, skip: Int): Result<ProductListResult> {
        return safeApiCall(
            onFailureFallback = {
                val cached = productCache.getAll()
                if (cached.isNotEmpty()) {
                    ProductListResult(products = cached, total = cached.size)
                } else null
            },
        ) {
            val response = productApi.getProducts(limit, skip)
            val products = response.products.map { dto ->
                val product = dto.toDomain()
                productCache.put(product.id, product)
                product
            }
            ProductListResult(products = products, total = response.total)
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        productCache.get(id)?.let { return Result.Success(it) }

        return safeApiCall(
            onFailureFallback = { productCache.get(id) },
        ) {
            val product = productApi.getProductById(id).toDomain()
            productCache.put(product.id, product)
            product
        }
    }

    override suspend fun searchProducts(query: String): Result<ProductListResult> {
        return safeApiCall(
            onFailureFallback = {
                val cached = productCache.getAll().filter { product ->
                    product.title.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true) ||
                        product.brand.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true)
                }
                if (cached.isNotEmpty()) {
                    ProductListResult(products = cached, total = cached.size)
                } else null
            },
        ) {
            val response = productApi.searchProducts(query)
            val products = response.products.map { dto ->
                val product = dto.toDomain()
                productCache.put(product.id, product)
                product
            }
            ProductListResult(products = products, total = response.total)
        }
    }

    override suspend fun getCategories(): Result<List<Category>> {
        categoryCache.get(CATEGORIES_CACHE_KEY)?.let { return Result.Success(it) }

        return safeApiCall(
            onFailureFallback = {
                categoryCache.get(CATEGORIES_CACHE_KEY)
                    ?: productCache.getAll()
                        .map { Category(slug = it.category, name = it.category) }
                        .distinctBy { it.slug }
                        .ifEmpty { null }
            },
        ) {
            val categories = productApi.getCategories().map { it.toDomain() }
            categoryCache.put(CATEGORIES_CACHE_KEY, categories)
            categories
        }
    }

    override suspend fun getProductsByCategory(category: String): Result<ProductListResult> {
        return safeApiCall(
            onFailureFallback = {
                val cached = productCache.getAll().filter { product ->
                    product.category.equals(category, ignoreCase = true)
                }
                if (cached.isNotEmpty()) {
                    ProductListResult(products = cached, total = cached.size)
                } else null
            },
        ) {
            val response = productApi.getProductsByCategory(category)
            val products = response.products.map { dto ->
                val product = dto.toDomain()
                productCache.put(product.id, product)
                product
            }
            ProductListResult(products = products, total = response.total)
        }
    }

    private inline fun <T> safeApiCall(
        onFailureFallback: () -> T? = { null },
        block: () -> T,
    ): Result<T> {
        return try {
            Result.Success(block())
        } catch (exception: ClientRequestException) {
            when (exception.response.status) {
                HttpStatusCode.NotFound -> {
                    onFailureFallback()?.let { return Result.Success(it) }
                    Result.Error(AppError.NotFound(exception.message))
                }
                else -> {
                    onFailureFallback()?.let { return Result.Success(it) }
                    Result.Error(AppError.Network(exception.message))
                }
            }
        } catch (exception: ServerResponseException) {
            onFailureFallback()?.let { return Result.Success(it) }
            Result.Error(AppError.Network(exception.message))
        } catch (exception: Exception) {
            onFailureFallback()?.let { return Result.Success(it) }
            Result.Error(AppError.Network(exception.message ?: "Network error occurred"))
        }
    }

    private companion object {
        const val CATEGORIES_CACHE_KEY = "all_categories"
    }
}

