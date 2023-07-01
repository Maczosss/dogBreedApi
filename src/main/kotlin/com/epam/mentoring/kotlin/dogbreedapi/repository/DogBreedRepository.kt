package com.epam.mentoring.kotlin.dogbreedapi.repository

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import kotlinx.coroutines.flow.Flow
//import org.springframework.data.jpa.repository.Query
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository


interface DogBreedRepository: CoroutineCrudRepository<DogBreed, String> {

    @Query("""
        SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'breed_db' AND table_name = 'dog_breed';
    """)
    suspend fun isThereDogBreedTable(): Flow<Integer>

    @Query("""CREATE TABLE `dog_breed` (
  `id` varchar(36) NOT NULL,
  `breed` varchar(255) DEFAULT NULL,
  `created` datetime(6) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `modified` datetime(6) DEFAULT NULL,
  `sub_breed` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci""")
    suspend fun createDogBreedTable()

    @Query("SELECT * FROM dog_breed n WHERE sub_breed=''")
    suspend fun findAllBreedsWhereThereAreNoSubBreeds():Flow<DogBreed>

    @Query("SELECT sub_breed FROM dog_breed WHERE sub_breed !='';")
    suspend fun getOnlySubBreeds(): Flow<String>

    @Query("SELECT sub_breed FROM dog_breed WHERE breed = :breed")
    suspend fun getBreedsSubBreeds(breed: String): Flow<String>

    @Query("SELECT * FROM dog_breed WHERE breed = :breed LIMIT 1")
    suspend fun getBreedByName(breed: String): DogBreed
}