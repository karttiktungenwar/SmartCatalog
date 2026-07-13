package com.app.smartcatalog.app.di

import com.app.smartcatalog.core.cache.InMemoryCache
import com.app.smartcatalog.core.network.createHttpClient
import com.app.smartcatalog.features.products.data.remote.ProductApi
import com.app.smartcatalog.features.products.data.repository.ProductRepositoryImpl
import com.app.smartcatalog.features.products.domain.model.Category
import com.app.smartcatalog.features.products.domain.model.Product
import com.app.smartcatalog.features.products.domain.repository.ProductRepository
import com.app.smartcatalog.features.products.domain.usecase.GetCategoriesUseCase
import com.app.smartcatalog.features.products.domain.usecase.GetProductByIdUseCase
import com.app.smartcatalog.features.products.domain.usecase.GetProductsByCategoryUseCase
import com.app.smartcatalog.features.products.domain.usecase.GetProductsUseCase
import com.app.smartcatalog.features.products.domain.usecase.SearchProductsUseCase
import com.app.smartcatalog.features.products.presentation.detail.ProductDetailViewModel
import com.app.smartcatalog.features.products.presentation.list.ProductListViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private var isKoinInitialized = false

fun initKoin() {
    if (isKoinInitialized) return
    startKoin {
        modules(appModule)
    }
    isKoinInitialized = true
}

val appModule = module {
    single<HttpClient> { createHttpClient() }
    single { ProductApi(get()) }
    single { InMemoryCache<Int, Product>() }
    single { InMemoryCache<String, List<Category>>() }
    single<ProductRepository> { ProductRepositoryImpl(get(), get(), get()) }

    factory { GetProductsUseCase(get()) }
    factory { SearchProductsUseCase(get()) }
    factory { GetProductByIdUseCase(get()) }
    factory { GetCategoriesUseCase(get()) }
    factory { GetProductsByCategoryUseCase(get()) }

    viewModel { ProductListViewModel(get(), get(), get(), get()) }
    viewModel { ProductDetailViewModel(get()) }
}
