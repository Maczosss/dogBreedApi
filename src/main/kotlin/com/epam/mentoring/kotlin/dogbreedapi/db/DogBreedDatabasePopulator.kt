package com.epam.mentoring.kotlin.dogbreedapi.db

import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
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
//        if(repository.findAll().count()==0){
//            val check = dogBreedApi.getBreeds()
//            if(check.isEmpty()){
//                println("Failed to load database entries from api")
//            }else{
//                println("Successfully added entries to database")
//            }
//        }
//        testPopulator()
//        BeanFactoryPostProcessor(BeanFa)
    }


    suspend fun testPopulator() = coroutineScope {
        if (repository.findAll().toList().isEmpty()) {
            val check = dogBreedApi.populateDogBreedTable()
            if (check.message.isEmpty()) {
                println("Failed to load database entries from api")
            } else {
                println("Successfully added entries to database")
            }
        }
    }

//    companion object {
//
//        @Autowired
//        lateinit var repository: DogBreedRepository
//
//        @Autowired
//        lateinit var dogBreedApi: DogBreedApiClient
//        suspend fun test() =  coroutineScope {
//                if (repository.findAll().toList().isEmpty()) {
//                val check = dogBreedApi.getBreeds()
//                if (check.isEmpty()) {
//                    println("Failed to load database entries from api")
//                } else {
//                    println("Successfully added entries to database")
//                }
//            }
//            else{
//                println("got data from db")
//                }
//        }
//    }
}