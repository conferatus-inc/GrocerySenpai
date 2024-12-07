package org.example.mainbackend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.example.mainbackend.dto.ProductDto
import org.example.mainbackend.dto.SimpleUserDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import java.util.Date

@Entity
@Table(
    indexes = [
        Index(name = "idx_product_user_id", columnList = "user_id"),
        Index(name = "idx_product_is_active_user_id", columnList = "is_active, user_id"),
    ],
)
data class Product(
    @Id
    @GeneratedValue
    val id: Long?,
    @Column(nullable = false)
    var category: String,
    @Column(nullable = false)
    var boughtOn: Date,
    @Column(nullable = false)
    var isActive: Boolean,
    //    @JoinColumn(nullable = false)
    @ManyToOne
    var user: User?,
    @Column(nullable = false)
    var isDeleted: Boolean = false,
    @CreatedDate
    var created: Instant = Instant.now(),
    @LastModifiedDate
    var updated: Instant = Instant.now(),
) {
    constructor(productDto: ProductDto) : this(
        id = productDto.id,
        category = productDto.category,
        boughtOn = productDto.boughtOn,
        isActive = productDto.isActive,
        user = null,
    )
}

fun Product.toProductDto(): ProductDto {
    return ProductDto(
        id = id,
        category = category,
        boughtOn = boughtOn,
        isActive = isActive,
        user = if (user != null) SimpleUserDto(user!!) else null,
        isDeleted = isDeleted,
    )
}
