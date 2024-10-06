package org.example.mainbackend.dto

import java.time.LocalDateTime

data class RecommendationDto(
    val items: List<SingleRecommendation>,
)

data class SingleRecommendation(
    val category: String,
    val nextBuy: LocalDateTime,
)
