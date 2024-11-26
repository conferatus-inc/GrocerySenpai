package org.example.mainbackend.service

import org.example.mainbackend.model.Product
import org.example.mainbackend.model.User
import org.example.mainbackend.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
        val res = productRepository.findById(id)
        productRepository.deleteById(id)
        return res.get()
    }

    @Transactional
    fun editProduct(
        product: Product,
        user: User,
    ): Product {
        product.user = user
        val prod = productRepository.findById(product.id!!).get()
        prod.category = product.category
        prod.boughtOn = product.boughtOn
        prod.isActive = product.isActive
        return productRepository.save(prod)
    }

    fun findByUser(user: User): List<Product> {
        return productRepository.findProductsByUser(user)
    }

    fun findActiveByUser(user: User): List<Product> {
        return productRepository.findProductsByIsActiveAndUser(true, user)
    }
}
