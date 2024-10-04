package org.example.mainbackend.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

@Configuration
@PropertySources(
    value = [
        PropertySource("classpath:properties.d/application.properties"),
        PropertySource("classpath:properties.d/local/local.properties"),
    ],
)
class PropertiesConfig
