package org.example.mainbackend.dto

import org.example.mainbackend.dto.enums.ChangeType
import java.time.Instant

data class ChangeDto(
    val product: ProductDto,
    val changeType: ChangeType,
    val changeTime: Instant,
)
