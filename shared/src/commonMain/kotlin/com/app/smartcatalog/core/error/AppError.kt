package com.app.smartcatalog.core.error

sealed class AppError(message: String) : Exception(message) {
    class Network(message: String = "Network error occurred") : AppError(message)
    class NotFound(message: String = "Resource not found") : AppError(message)
    class Unknown(message: String = "An unexpected error occurred") : AppError(message)
}
