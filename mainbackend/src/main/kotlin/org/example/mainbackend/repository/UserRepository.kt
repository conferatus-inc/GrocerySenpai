package org.example.mainbackend.repository

import org.example.mainbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?

    fun deleteByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean
}
