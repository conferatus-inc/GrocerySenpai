package org.example.mainbackend.repository

import org.example.mainbackend.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>
