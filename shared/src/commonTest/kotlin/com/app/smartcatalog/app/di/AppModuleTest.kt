package com.app.smartcatalog.app.di

import com.app.smartcatalog.core.cache.InMemoryCache
import com.app.smartcatalog.features.products.domain.model.Category
import com.app.smartcatalog.features.products.domain.model.Product
import com.app.smartcatalog.features.products.domain.repository.ProductRepository
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame

class AppModuleTest {

    @Test
    fun `productCache and categoryCache are distinct instances`() {
        val koinApp = koinApplication {
            modules(appModule)
        }
        val koin = koinApp.koin

        val productCache: InMemoryCache<Int, Product> = koin.get(named("productCache"))
        val categoryCache: InMemoryCache<String, List<Category>> = koin.get(named("categoryCache"))

        assertNotSame(productCache, categoryCache)

        koinApp.close()
    }

    @Test
    fun `productRepository is resolved successfully`() {
        val koinApp = koinApplication {
            modules(appModule)
        }
        val koin = koinApp.koin

        val repository: ProductRepository = koin.get()
        assertNotNull(repository)

        koinApp.close()
    }
}
