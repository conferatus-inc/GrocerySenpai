package org.example.mainbackend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "t_user",
    indexes = [
        Index(name = "idx_user_username", columnList = "username"),
    ],
)
data class User(
    @Id
    @GeneratedValue
    val id: Long? = null,
    @Size(min = 5, message = "At least 5 characters")
    @Column(nullable = false)
    var username: String,
    @Column(nullable = false)
    var accessToken: String? = null,
    @Column(nullable = false)
    var refreshToken: String? = null,
    @ManyToMany(fetch = FetchType.EAGER)
    var roles: MutableSet<Role> = mutableSetOf(),
    @OneToMany(fetch = FetchType.EAGER)
    val products: MutableSet<Product> = mutableSetOf(),
)
