package org.example.mainbackend.client

import org.example.mainbackend.dto.ProductsDto
import org.example.mainbackend.dto.RecommendationsDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(value = "recommendations")
interface RecommendationsClient {
    @PostMapping("/recommendations")
    fun startRecommendationGeneration(products: ProductsDto): Long

    @GetMapping("/result/{taskId}")
    fun getRecommendation(
        @PathVariable("taskId") taskId: Long,
    ): RecommendationsDto
}
