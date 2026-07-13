package com.app.smartcatalog.core.cache

class InMemoryCache<K, V> {
    private val store = mutableMapOf<K, V>()

    fun get(key: K): V? = store[key]

    fun put(key: K, value: V) {
        store[key] = value
    }

    fun clear() {
        store.clear()
    }
}
