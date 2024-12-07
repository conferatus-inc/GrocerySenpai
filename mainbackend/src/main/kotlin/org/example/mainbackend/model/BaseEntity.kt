package org.example.mainbackend.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

open class BaseEntity(
    @CreatedDate
    var created: Instant = Instant.now(),
    @LastModifiedDate
    var updated: Instant = Instant.now(),
)
