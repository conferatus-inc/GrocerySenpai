package org.example.mainbackend.repository

import org.example.mainbackend.model.Product
import org.example.mainbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface ProductRepository : JpaRepository<Product, Long> {
    fun findProductsByUserId(userId: Long): List<Product>

    fun findProductsByUserIdAndUpdatedAfter(
        userId: Long,
        updated: Instant,
    ): List<Product>

    fun findProductsByIsDeletedAndUser(
        isDeleted: Boolean,
        user: User,
    ): List<Product>

    fun findProductsByIsActiveAndIsDeletedAndUser(
        isActive: Boolean,
        isDeleted: Boolean,
        user: User,
    ): List<Product>
}
