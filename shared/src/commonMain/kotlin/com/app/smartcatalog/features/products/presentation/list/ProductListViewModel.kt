package com.app.smartcatalog.features.products.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.smartcatalog.core.error.Result
import com.app.smartcatalog.features.products.domain.usecase.GetCategoriesUseCase
import com.app.smartcatalog.features.products.domain.usecase.GetProductsByCategoryUseCase
import com.app.smartcatalog.features.products.domain.usecase.GetProductsUseCase
import com.app.smartcatalog.features.products.domain.usecase.SearchProductsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState(isLoading = true))
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCategories()
        loadProducts()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isBlank() && _uiState.value.selectedCategory == null) {
            loadProducts()
        }
    }

    fun onSearchSubmit() {
        val query = _uiState.value.searchQuery
        if (query.isBlank()) {
            onCategorySelected(null)
            return
        }
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, selectedCategory = null) }
            when (val result = searchProductsUseCase(query)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        products = result.data.products,
                        totalProducts = result.data.total,
                        isLoading = false,
                    )
                }
                is Result.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.error.message,
                    )
                }
            }
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                searchQuery = if (category != null) "" else it.searchQuery,
            )
        }
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = if (category == null) {
                getProductsUseCase()
            } else {
                getProductsByCategoryUseCase(category)
            }
            when (result) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        products = result.data.products,
                        totalProducts = result.data.total,
                        isLoading = false,
                    )
                }
                is Result.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.error.message,
                    )
                }
            }
        }
    }

    fun retry() {
        if (_uiState.value.searchQuery.isNotBlank()) {
            onSearchSubmit()
        } else {
            onCategorySelected(_uiState.value.selectedCategory)
        }
    }

    private fun loadProducts() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getProductsUseCase()) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        products = result.data.products,
                        totalProducts = result.data.total,
                        isLoading = false,
                    )
                }
                is Result.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.error.message,
                    )
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            when (val result = getCategoriesUseCase()) {
                is Result.Success -> _uiState.update { it.copy(categories = result.data) }
                is Result.Error -> Unit
            }
        }
    }
}
