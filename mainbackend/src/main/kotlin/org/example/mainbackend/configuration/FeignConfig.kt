package org.example.mainbackend.configuration

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["org.example.mainbackend.client"])
class FeignConfig
