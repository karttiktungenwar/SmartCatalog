package com.app.smartcatalog.features.products.data.repository

import com.app.smartcatalog.core.cache.InMemoryCache
import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.data.remote.ProductApi
import com.app.smartcatalog.features.products.domain.model.Category
import com.app.smartcatalog.features.products.domain.model.Product
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductRepositoryImplTest {

    private fun createProductApi(shouldFail: Boolean, responseJson: String = ""): ProductApi {
        val mockEngine = MockEngine { request ->
            if (shouldFail) {
                respondError(HttpStatusCode.InternalServerError, "Network unavailable")
            } else {
                respond(
                    content = responseJson,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return ProductApi(httpClient)
    }

    @Test
    fun `getProducts fetches from API and caches products`() = runTest {
        val json = """
            {
                "products": [
                    {
                        "id": 1,
                        "title": "Laptop",
                        "description": "Powerful laptop",
                        "category": "laptops",
                        "price": 999.99,
                        "rating": 4.8,
                        "brand": "TechCorp",
                        "thumbnail": "https://example.com/laptop.jpg",
                        "images": []
                    }
                ],
                "total": 1,
                "skip": 0,
                "limit": 30
            }
        """.trimIndent()

        val productCache = InMemoryCache<Int, Product>()
        val categoryCache = InMemoryCache<String, List<Category>>()
        val repository = ProductRepositoryImpl(
            productApi = createProductApi(shouldFail = false, responseJson = json),
            productCache = productCache,
            categoryCache = categoryCache,
        )

        val result = repository.getProducts()

        assertTrue(result is Result.Success)
        assertEquals(1, result.data.products.size)
        assertEquals("Laptop", result.data.products.first().title)
        assertEquals(1, productCache.size())
    }

    @Test
    fun `getProducts falls back to cache when offline`() = runTest {
        val productCache = InMemoryCache<Int, Product>()
        val categoryCache = InMemoryCache<String, List<Category>>()

        val cachedProduct = Product(
            id = 10,
            title = "Cached Tablet",
            description = "A tablet stored in cache",
            category = "tablets",
            price = 299.99,
            rating = 4.2,
            brand = "CacheBrand",
            thumbnail = "https://example.com/tablet.jpg",
            images = emptyList(),
        )
        productCache.put(cachedProduct.id, cachedProduct)

        val repository = ProductRepositoryImpl(
            productApi = createProductApi(shouldFail = true),
            productCache = productCache,
            categoryCache = categoryCache,
        )

        val result = repository.getProducts()

        assertTrue(result is Result.Success)
        assertEquals(1, result.data.products.size)
        assertEquals("Cached Tablet", result.data.products.first().title)
    }

    @Test
    fun `searchProducts falls back to cache filtering when offline`() = runTest {
        val productCache = InMemoryCache<Int, Product>()
        val categoryCache = InMemoryCache<String, List<Category>>()

        val product1 = Product(
            id = 1,
            title = "Wireless Headphones",
            description = "Noise cancelling",
            category = "audio",
            price = 150.0,
            rating = 4.5,
            brand = "SoundPro",
            thumbnail = "https://example.com/headphones.jpg",
            images = emptyList(),
        )
        val product2 = Product(
            id = 2,
            title = "Smart Watch",
            description = "Fitness tracking",
            category = "wearables",
            price = 199.0,
            rating = 4.0,
            brand = "WristTech",
            thumbnail = "https://example.com/watch.jpg",
            images = emptyList(),
        )
        productCache.put(product1.id, product1)
        productCache.put(product2.id, product2)

        val repository = ProductRepositoryImpl(
            productApi = createProductApi(shouldFail = true),
            productCache = productCache,
            categoryCache = categoryCache,
        )

        val searchResult = repository.searchProducts("headphones")

        assertTrue(searchResult is Result.Success)
        assertEquals(1, searchResult.data.products.size)
        assertEquals("Wireless Headphones", searchResult.data.products.first().title)
    }

    @Test
    fun `getCategories caching does not corrupt productCache fallback when offline`() = runTest {
        val productCache = InMemoryCache<Int, Product>()
        val categoryCache = InMemoryCache<String, List<Category>>()

        // Simulate categories already cached in categoryCache
        categoryCache.put("all_categories", listOf(Category(slug = "beauty", name = "Beauty")))

        val repository = ProductRepositoryImpl(
            productApi = createProductApi(shouldFail = true),
            productCache = productCache,
            categoryCache = categoryCache,
        )

        val result = repository.getProducts()

        // Since productCache is empty, fallback should fail with Error instead of returning List<Category>
        assertTrue(result is Result.Error)
    }
}
