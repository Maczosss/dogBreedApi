package com.epam.mentoring.kotlin.dogbreedapi.db

import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DogBreedDatabasePopulator(private val dogBreedApi: DogBreedApiClient, private val repository: DogBreedRepository) {
    @Bean
    fun initializeDatabase(): ApplicationRunner {
        return ApplicationRunner {
            runBlocking {
                if (repository.isThereDogBreedTable().toList()[0].toInt() == 0) {
                    println("There is no table, creating... ")
                    repository.createDogBreedTable()
                }
                if (repository.findAll().toList().isEmpty()) {
                    println("Table 'dog_breed' is empty, started populating...")
                    dogBreedApi.populateDogBreedTable()
                }
                println("Database is populated")
            }
        }
    }
}