package org.example.mainbackend.service

import org.example.mainbackend.client.RecommendationsClient
import org.example.mainbackend.dto.Products
import org.example.mainbackend.dto.Recommendation
import org.springframework.stereotype.Service

@Service
class RecommendationsService(
    private val recommendationsClient: RecommendationsClient,
) {
    fun doRecommendation(): Long {
        return recommendationsClient.createTask(products = Products(listOf()))
    }

    fun getRecommendation(taskId: Long): Recommendation {
        return recommendationsClient.getResult(taskId)
    }
}
