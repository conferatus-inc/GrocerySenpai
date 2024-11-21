package org.example.mainbackend.controller

import org.example.mainbackend.dto.ProductDto
import org.example.mainbackend.dto.ProductsDto
import org.example.mainbackend.model.Product
import org.example.mainbackend.service.ProductsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/products")
class ProductsController(
    private val productsService: ProductsService,
) {
    @PostMapping
    fun addProduct(
        @RequestBody purchase: ProductDto,
        principal: Principal,
    ): ProductDto {
        return ProductDto(productsService.addProductToUser(Product(purchase), principal.name))
    }

    @GetMapping
    fun getProducts(principal: Principal): ProductsDto {
        return ProductsDto(productsService.findByUser(principal.name).map { ProductDto(it) })
    }
}
