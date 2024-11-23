package org.example.mainbackend.aboba

import org.example.mainbackend.model.Role
import org.example.mainbackend.model.enums.RoleName
import org.example.mainbackend.repository.RoleRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ApplRunner(
    private val roleRepository: RoleRepository,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        roleRepository.save(Role(name = RoleName.ROLE_USER))
        roleRepository.save(Role(name = RoleName.ROLE_ADMIN))
        roleRepository.save(Role(name = RoleName.ROLE_ROOT))
    }
}
