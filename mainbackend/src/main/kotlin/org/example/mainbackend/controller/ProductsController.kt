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

@RestController
@RequestMapping("/api/v1/products")
class ProductsController(
    private val productsService: ProductsService,
) {
    @PostMapping
    fun addProduct(
        @RequestBody purchase: ProductDto,
    ): ProductDto {
        return ProductDto(productsService.addProduct(Product(purchase)))
    }

    @GetMapping
    fun getProducts(): ProductsDto {
        return ProductsDto(productsService.findAll().map { ProductDto(it) })
    }
}
