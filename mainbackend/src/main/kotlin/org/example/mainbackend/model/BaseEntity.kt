package org.example.mainbackend.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

open class BaseEntity(
    @CreatedDate
    open var created: Instant = Instant.now(),
    @LastModifiedDate
    open var updated: Instant = Instant.now(),
)
