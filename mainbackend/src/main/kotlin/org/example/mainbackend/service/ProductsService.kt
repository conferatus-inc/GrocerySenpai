package org.example.mainbackend.service

import org.example.mainbackend.model.Product
import org.example.mainbackend.model.User
import org.example.mainbackend.repository.ProductRepository
import org.example.mainbackend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ProductsService(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) {
    fun addProductToUser(
        product: Product,
        user: User,
    ): Product {
        product.user = user
        user.products.add(product)
        val saved = productRepository.save(product)
        userRepository.save(user)
        return saved
    }

    fun deleteProductByIdAndUser(
        id: Long,
        user: User,
    ): Product {
        val res = productRepository.findById(id).get()
        res.isDeleted = true
        return productRepository.save(res)
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

    fun findAllByUserAndTime(
        user: User,
        fromTime: Instant,
    ): List<Product> {
        val a = productRepository.findProductsByUserId(user.id!!)
        return productRepository.findProductsByUserIdAndUpdatedAfter(
            userId = user.id!!,
            updated = fromTime,
        )
    }

    fun findByUser(user: User): List<Product> {
        return productRepository.findProductsByIsDeletedAndUser(
            isDeleted = false,
            user = user,
        )
    }

    fun findActiveByUser(user: User): List<Product> {
        return productRepository.findProductsByIsActiveAndIsDeletedAndUser(
            isActive = true,
            isDeleted = false,
            user = user,
        )
    }
}
