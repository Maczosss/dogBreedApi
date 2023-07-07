package com.epam.mentoring.kotlin.dogbreedapi.db

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.time.ZoneId
import java.util.*


@Configuration
class DatabaseConfig {

    @Bean
    fun connectionFactory(): ConnectionFactory? {
        val props = Properties()
        try {
            props.load(
                FileInputStream("PATH_TO_PROPERTIES")
            )
        }catch(e: FileNotFoundException){
            println("Could not find application.properties file.")
        }
        return MySqlConnectionFactory.from(
            MySqlConnectionConfiguration.builder()
                .host(props.getProperty("db.host"))
                .port(props.getProperty("db.port").toInt())
                .username(props.getProperty("db.username"))
                .password(props.getProperty("db.password"))
                .database(props.getProperty("db.database"))
                .serverZoneId(ZoneId.of("UTC"))
                .build()
        )
    }
}