package com.epam.mentoring.kotlin.dogbreedapi

import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class DogBreedApiApplication

fun main(args: Array<String>) {
	runApplication<DogBreedApiApplication>(*args)

}
