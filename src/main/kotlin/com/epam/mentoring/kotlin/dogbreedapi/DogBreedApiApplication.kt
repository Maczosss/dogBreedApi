package com.epam.mentoring.kotlin.dogbreedapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@EnableCaching
@EnableR2dbcRepositories
@SpringBootApplication
class DogBreedApiApplication

fun main(args: Array<String>) {
	runApplication<DogBreedApiApplication>(*args)
}
