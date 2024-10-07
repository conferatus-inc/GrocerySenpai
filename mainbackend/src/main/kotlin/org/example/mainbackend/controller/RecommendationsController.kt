package org.example.mainbackend.controller

import org.example.mainbackend.dto.RecommendationDto
import org.example.mainbackend.service.RecommendationsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/recommendations")
class RecommendationsController(
    private val recommendationsService: RecommendationsService,
) {
    @GetMapping("/start")
    fun startRecommendationGeneration(): Long {
        return recommendationsService.startRecommendationGeneration()
    }

    @GetMapping("/get_result/{taskId}")
    fun getRecommendation(
        @PathVariable taskId: Long,
    ): RecommendationDto {
        return recommendationsService.getRecommendation(taskId)
    }
}
