package org.example.mainbackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MainbackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(MainbackendApplication::class.java, *args)
//    runApplication<MainbackendApplication>(*args)
}
