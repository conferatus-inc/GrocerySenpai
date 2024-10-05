package org.example.mainbackend.controller

import org.example.mainbackend.dto.Recommendation
import org.example.mainbackend.service.RecommendationsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recommendations")
class RecommendationsController(
    private val recommendationsService: RecommendationsService,
) {
    @GetMapping("/start")
    fun doRecommendation(): Long {
        return recommendationsService.doRecommendation()
    }

    @GetMapping("/get_result/{taskId}")
    fun getResult(
        @PathVariable taskId: Long,
    ): Recommendation {
        return recommendationsService.getRecommendation(taskId)
    }
}
