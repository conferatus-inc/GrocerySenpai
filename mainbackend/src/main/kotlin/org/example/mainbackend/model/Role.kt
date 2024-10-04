package org.example.mainbackend.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.example.mainbackend.model.enums.RoleName
import org.springframework.data.annotation.Transient
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(
    name = "role",
    uniqueConstraints = [
        UniqueConstraint(name = "uc_role_name", columnNames = ["name"]),
    ],
)
data class Role(
    @Id
    @GeneratedValue
    val id: Long? = null,
    @Enumerated(EnumType.STRING)
    val name: RoleName,
    @Transient
    @ManyToMany(mappedBy = "roles")
    val users: MutableSet<User> = mutableSetOf(),
) : GrantedAuthority {
    override fun getAuthority(): String {
        return name.name
    }
}
