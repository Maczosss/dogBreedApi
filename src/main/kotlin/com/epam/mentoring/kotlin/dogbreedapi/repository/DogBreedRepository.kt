package com.epam.mentoring.kotlin.dogbreedapi.repository

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository


interface DogBreedRepository: CrudRepository<DogBreed, String> {

    @Query("SELECT * FROM dog_breed n WHERE sub_breed=''", nativeQuery = true)
    fun findAllBreedsWhereThereAreNoSubBreeds():Iterable<DogBreed>

    @Query("SELECT sub_breed FROM dog_breed WHERE sub_breed !='';", nativeQuery = true)
    fun getOnlySubBreeds(): Iterable<String>

    @Query("SELECT sub_breed FROM dog_breed WHERE breed =?1", nativeQuery = true)
    fun getBreedsSubBreeds(breed: String): Iterable<String>

    @Query("SELECT * FROM dog_breed WHERE breed =?1 LIMIT 1", nativeQuery = true)
    fun getBreedByName(breed: String): DogBreed
}