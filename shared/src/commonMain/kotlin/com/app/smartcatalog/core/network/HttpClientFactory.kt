package com.app.smartcatalog.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun createHttpClientEngine(): HttpClientEngine

fun createHttpClient(engine: HttpClientEngine = createHttpClientEngine()): HttpClient {
    return HttpClient(engine) {
        defaultRequest {
            url(ApiConstants.BASE_URL)
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    coerceInputValues = true
                }
            )
        }
    }
}
