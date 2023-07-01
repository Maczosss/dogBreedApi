package com.epam.mentoring.kotlin.dogbreedapi.db

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseInitializer(private val dogBreedApi: DogBreedApiClient, private val repository: DogBreedRepository) {
    @Bean
    fun initializeDatabase(): ApplicationRunner {
        return ApplicationRunner {
            runBlocking {
                if (repository.isThereDogBreedTable().toList()[0].toInt() == 0) {
                    println("There no table ")
                    repository.createDogBreedTable()
                }
                if (repository.findAll().toList().isEmpty()) {
                    val breedsResponse = dogBreedApi.getBreeds()

                    if (breedsResponse.status == "success") {
                        val dbBreeds = breedsResponse.message.map { (breed, subBreeds) ->
                            DogBreed(
                                breed = breed,
                                subBreed = subBreeds.joinToString(", ")
                            )
                        }
                        repository.saveAll(dbBreeds)
                    }
                }
                println("Database is populated")
            }
        }
    }
}