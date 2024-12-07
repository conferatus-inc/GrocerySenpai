package org.example.mainbackend.controller

import org.example.mainbackend.dto.ChangeDto
import org.example.mainbackend.model.User
import org.example.mainbackend.service.ChangesService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/v1/changes")
class ChangesController(
    private val changesService: ChangesService,
) {
    @PostMapping("/changes")
    fun makeChanges(
        @AuthenticationPrincipal user: User,
        @RequestBody changes: List<ChangeDto>,
    ) {
        changesService.makeChanges(user, changes)
    }

    @GetMapping("/changes")
    fun getChanges(
        @AuthenticationPrincipal user: User,
        @RequestBody fromTime: Instant,
    ): List<ChangeDto> {
        return changesService.getChanges(user, fromTime)
    }
}
