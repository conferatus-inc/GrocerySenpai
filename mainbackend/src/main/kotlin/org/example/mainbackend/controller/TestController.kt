package org.example.mainbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/")
    fun home(): String {
        return "home"
    }

    @GetMapping("/secured")
    fun secured(): String {
        return "Secured"
    }
}
