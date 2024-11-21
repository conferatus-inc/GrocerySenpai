package org.example.mainbackend.service

import org.example.mainbackend.model.Product
import org.example.mainbackend.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductsService(
    private val productRepository: ProductRepository,
    private val accountService: AccountService,
) {
    fun addProductToUser(
        product: Product,
        username: String,
    ): Product {
        product.user = accountService.getUser(username)
        return productRepository.save(product)
    }

    fun findAll(): List<Product> {
        return productRepository.findAll()
    }

    fun fundByUser(username: String): List<Product> {
        return productRepository.findProductsByUser(accountService.getUser(username))
    }
}
