package com.app.smartcatalog.features.products.data.mapper

import com.app.smartcatalog.features.products.data.dto.CategoryDto
import com.app.smartcatalog.features.products.data.dto.ProductDto
import com.app.smartcatalog.features.products.domain.model.Category
import com.app.smartcatalog.features.products.domain.model.Product

fun ProductDto.toDomain(): Product = Product(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    rating = rating,
    brand = brand.orEmpty(),
    thumbnail = thumbnail,
    images = images,
)

fun CategoryDto.toDomain(): Category = Category(
    slug = slug,
    name = name,
)
