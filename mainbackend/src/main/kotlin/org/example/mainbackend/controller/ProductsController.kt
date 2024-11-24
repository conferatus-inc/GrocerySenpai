package org.example.mainbackend.controller

import org.example.mainbackend.dto.ProductDto
import org.example.mainbackend.dto.ProductsDto
import org.example.mainbackend.model.Product
import org.example.mainbackend.model.User
import org.example.mainbackend.service.ProductsService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductsController(
    private val productsService: ProductsService,
) {
    @PostMapping
    fun addProduct(
        @RequestBody purchase: ProductDto,
        @AuthenticationPrincipal user: User,
    ): ProductDto {
        return ProductDto(productsService.addProductToUser(Product(purchase), user))
    }

    @GetMapping
    fun getProducts(
        @AuthenticationPrincipal user: User,
    ): ProductsDto {
        return ProductsDto(productsService.findByUser(user).map { ProductDto(it) })
    }

    @GetMapping("/all")
    fun getAllProducts(): ProductsDto {
        return ProductsDto(productsService.findAll().map { ProductDto(it) })
    }
}
