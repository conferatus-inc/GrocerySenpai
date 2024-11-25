package org.example.mainbackend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.example.mainbackend.dto.ProductDto
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
    val category: String,
    @Column(nullable = false)
    val boughtOn: Date,
    @Column(nullable = false)
    val isActive: Boolean,
    //    @JoinColumn(nullable = false)
    @ManyToOne
    var user: User?,
) {
    constructor(productDto: ProductDto) : this(
        id = productDto.id,
        category = productDto.category,
        boughtOn = productDto.boughtOn,
        isActive = productDto.isActive,
        user = null,
    )
}
