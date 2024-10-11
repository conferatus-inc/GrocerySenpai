package org.example.mainbackend.repository

import org.example.mainbackend.model.Role
import org.example.mainbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long>{

}
