package com.app.smartcatalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.app.smartcatalog.features.products.presentation.detail.ProductDetailScreen
import com.app.smartcatalog.features.products.presentation.list.ProductListScreen
import com.app.smartcatalog.features.products.presentation.theme.SmartCatalogTheme
import kotlinx.serialization.Serializable

@Serializable
object ProductListRoute

@Serializable
data class ProductDetailRoute(val productId: Int)

@Composable
@Preview
fun App() {
    SmartCatalogTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = ProductListRoute,
        ) {
            composable<ProductListRoute> {
                ProductListScreen(
                    onProductClick = { productId ->
                        navController.navigate(ProductDetailRoute(productId))
                    },
                )
            }
            composable<ProductDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<ProductDetailRoute>()
                ProductDetailScreen(
                    productId = route.productId,
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}
