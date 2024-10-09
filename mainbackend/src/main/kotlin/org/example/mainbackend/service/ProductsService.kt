package org.example.mainbackend.service

import org.example.mainbackend.model.Product
import org.example.mainbackend.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductsService(
    private val productRepository: ProductRepository,
) {
    fun addProduct(product: Product): Product {
        return productRepository.save(product)
    }

    fun findAll(): List<Product> {
        return productRepository.findAll()
    }
}
