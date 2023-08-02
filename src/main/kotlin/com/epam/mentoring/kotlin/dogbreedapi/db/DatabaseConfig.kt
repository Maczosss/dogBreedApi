package com.epam.mentoring.kotlin.dogbreedapi.db

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.ZoneId


@Configuration
class DatabaseConfig(val properties: AppProperties) {
    @Bean
    @Profile("!test")
    fun connectionFactory(): ConnectionFactory? {
        return MySqlConnectionFactory.from(
            MySqlConnectionConfiguration.builder()
                .host(properties.host)
                .port(properties.port.toInt())
                .username(properties.username)
                .password(properties.password)
                .database(properties.database)
                .serverZoneId(ZoneId.of("UTC"))
                .build()
        )
    }
    @Bean
    @Profile("test")
    //this bean is created for test purpose only
    fun connectionFactoryTest(): ConnectionFactory {
        return H2ConnectionFactory.inMemory("testdb","sb","")
    }
}