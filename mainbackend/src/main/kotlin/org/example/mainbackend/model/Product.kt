package org.example.mainbackend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.OffsetDateTime

@Entity
data class Product(
    @Id
    @GeneratedValue
    val id: Long? = null,
    @Column(nullable = false)
    val category: String,
    @Column(nullable = false)
    val boughtOn: OffsetDateTime,
    @ManyToOne
    @JoinColumn(nullable = false)
    val user: User,
)
