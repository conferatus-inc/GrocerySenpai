package org.example.mainbackend.client

import org.example.mainbackend.dto.Products
import org.example.mainbackend.dto.Recommendation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(value = "recommendations")
interface RecommendationsClient {
    @PostMapping("/recommendations")
    fun createTask(products: Products): Long

    @GetMapping("/result/{taskId}")
    fun getResult(
        @PathVariable("taskId") taskId: Long,
    ): Recommendation
}
