package com.epam.mentoring.kotlin.dogbreedapi.data_populator

import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DogBreedDatabasePopulator {

    @Autowired
    lateinit var dogBreedApi: DogBreedApiClient

    @Autowired
    lateinit var repository: DogBreedRepository

    @PostConstruct
    fun populateDatabase(){
        if(repository.findAll().count()==0){
            val check = dogBreedApi.getBreeds()
            if(check.isEmpty()){
                println("Failed to load database entries from api")
            }else{
                println("Successfully added entries to database")
            }
        }
    }
}