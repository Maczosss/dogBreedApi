package com.epam.mentoring.kotlin.dogbreedapi.db

import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DogBreedDatabasePopulator(private val dogBreedApi: DogBreedApiClient, private val repository: DogBreedRepository) {

    private val log = LoggerFactory.getLogger(this::class.java)
    @Bean
    fun initializeDatabase(): ApplicationRunner {
        return ApplicationRunner {
            runBlocking {
                log.info("Checking database...")
                if (repository.isThereDogBreedTable().toList()[0].toInt() == 0) {
                    log.warn("There is no table, creating... ")
                    repository.createDogBreedTable()
                }
                if (repository.findAll().toList().isEmpty()) {
                    log.warn("Table 'dog_breed' is empty, started populating...")
                    dogBreedApi.populateDogBreedTable()
                }
                log.info("Database is populated")
            }
        }
    }
}