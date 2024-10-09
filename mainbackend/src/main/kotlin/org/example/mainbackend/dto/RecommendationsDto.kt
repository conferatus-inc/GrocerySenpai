package org.example.mainbackend.dto

import java.time.LocalDateTime

data class RecommendationsDto(
    val items: List<RecommendationDto>,
)

data class RecommendationDto(
    val category: String,
    val nextBuy: LocalDateTime,
)
