package org.example.mainbackend.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.ManyToMany
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
    val username: String,
    @ManyToMany(fetch = FetchType.EAGER)
    val roles: MutableSet<Role> = mutableSetOf(),
)
