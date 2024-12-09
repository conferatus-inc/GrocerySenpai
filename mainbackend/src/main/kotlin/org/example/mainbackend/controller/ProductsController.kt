package org.example.mainbackend.controller

import org.example.mainbackend.dto.ProductDto
import org.example.mainbackend.model.User
import org.example.mainbackend.model.toProductDto
import org.example.mainbackend.service.ProductsService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductsController(
    private val productsService: ProductsService,
) {
    @GetMapping("/history")
    fun getProductHistory(
        @AuthenticationPrincipal user: User,
    ): List<ProductDto> {
        return productsService.findByUser(user).map { it.toProductDto() }
    }

    @GetMapping("/active")
    fun getActiveProducts(
        @AuthenticationPrincipal user: User,
    ): List<ProductDto> {
        return productsService.findActiveByUser(user).map { it.toProductDto() }
    }
}
