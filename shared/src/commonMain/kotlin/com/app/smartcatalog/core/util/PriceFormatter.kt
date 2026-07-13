package com.app.smartcatalog.core.util

fun Double.formatAsPrice(): String {
    val rounded = (this * 100).toLong()
    val dollars = rounded / 100
    val cents = (rounded % 100).toString().padStart(2, '0')
    return "$$dollars.$cents"
}
