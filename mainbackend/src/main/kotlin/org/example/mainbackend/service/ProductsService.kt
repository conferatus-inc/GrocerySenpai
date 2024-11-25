package org.example.mainbackend.service

import org.example.mainbackend.model.Product
import org.example.mainbackend.model.User
import org.example.mainbackend.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductsService(
    private val productRepository: ProductRepository,
) {
    fun addProductToUser(
        product: Product,
        user: User,
    ): Product {
        product.user = user
        return productRepository.save(product)
    }

    fun deleteProductByIdAndUser(
        id: Long,
        user: User,
    ): Product {
        return productRepository.deleteByIdAndUser(id, user)
    }

    fun editProduct(
        product: Product,
        user: User,
    ): Product {
        productRepository.deleteByIdAndUser(product.id!!, user)
        return productRepository.save(product)
    }

    fun findByUser(user: User): List<Product> {
        return productRepository.findProductsByUser(user)
    }

    fun findActiveByUser(user: User): List<Product> {
        return productRepository.findProductsByIsActiveAndUser(true, user)
    }
}
