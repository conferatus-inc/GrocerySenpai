package org.example.mainbackend.service

import org.example.mainbackend.client.RecommendationsClient
import org.example.mainbackend.dto.ProductsDto
import org.example.mainbackend.dto.RecommendationDto
import org.springframework.stereotype.Service

@Service
class RecommendationsService(
    private val recommendationsClient: RecommendationsClient,
) {
    fun startRecommendationGeneration(): Long {
        return recommendationsClient.startRecommendationGeneration(products = ProductsDto(listOf()))
    }

    fun getRecommendation(taskId: Long): RecommendationDto {
        return recommendationsClient.getRecommendation(taskId)
    }
}
