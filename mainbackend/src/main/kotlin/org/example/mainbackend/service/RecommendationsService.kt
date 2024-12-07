package org.example.mainbackend.service

import org.example.mainbackend.client.RecommendationsClient
import org.example.mainbackend.dto.ProductsDto
import org.example.mainbackend.dto.RecommendationsDto
import org.springframework.stereotype.Service

@Service
@Deprecated(message = "теперь это на мобилке")
class RecommendationsService(
    private val recommendationsClient: RecommendationsClient,
) {
    fun startRecommendationGeneration(): Long {
        return recommendationsClient.startRecommendationGeneration(products = ProductsDto(listOf()))
    }

    fun getRecommendation(taskId: Long): RecommendationsDto {
        return recommendationsClient.getRecommendation(taskId)
    }
}
