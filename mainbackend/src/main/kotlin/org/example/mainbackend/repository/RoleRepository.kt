package org.example.mainbackend.repository

import org.example.mainbackend.model.Role
import org.example.mainbackend.model.enums.RoleName
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: RoleName): Role?
}
