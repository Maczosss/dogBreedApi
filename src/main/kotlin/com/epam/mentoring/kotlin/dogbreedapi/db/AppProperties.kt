package com.epam.mentoring.kotlin.dogbreedapi.db

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource(value = ["classpath:application.properties"])
data class AppProperties(
    @Value("\${db.host}")
    var host: String,
    @Value("\${db.port}")
    var port: String,
    @Value("\${db.username}")
    var username: String,
    @Value("\${db.password}")
    var password: String,
    @Value("\${db.database}")
    var database: String
)