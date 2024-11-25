package org.example.mainbackend.repository

import org.example.mainbackend.model.Product
import org.example.mainbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findProductsByUser(user: User): List<Product>

    fun findProductsByIsActiveAndUser(
        isActive: Boolean,
        user: User,
    ): List<Product>

    fun deleteByIdAndUser(
        id: Long,
        user: User,
    ): Product
}
