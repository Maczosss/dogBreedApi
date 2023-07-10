package com.epam.mentoring.kotlin.dogbreedapi.service

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreedDTO
import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.error.DatabaseSaveException
import com.epam.mentoring.kotlin.dogbreedapi.error.NoBreedReturnedFromDatabaseException
import com.epam.mentoring.kotlin.dogbreedapi.error.NoPictureReturnedFromExternalSource
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.*
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class DogBreedService(
    private val repository: DogBreedRepository,
    private val apiClient: DogBreedApiClient) {


    suspend fun saveMapOfBreeds(breedsToSave: Map<String, List<String>>) {
        repository.saveAll(breedsToSave.map {
            DogBreed(
                breed = it.key,
                subBreed = it.value.joinToString(", "),
                image = null
            )
        })
    }

    @org.springframework.cache.annotation.Cacheable("breeds")
    suspend fun getBreeds(): List<DogBreedDTO> {
        val result = repository.findAll().map { DogBreedDTO(it) }.toList()
        if (result.isNotEmpty()){
            return result
        }
        throw NoBreedReturnedFromDatabaseException("Breed was not found in database")
    }

    @org.springframework.cache.annotation.Cacheable("nonSubBreed")
    suspend fun getBreedsWithNoSubBreeds(): Iterable<DogBreedDTO> {
        val result = repository.findAllBreedsWhereThereAreNoSubBreeds().map { DogBreedDTO(it) }.toList()
        if(result.isNotEmpty()){
            return result
        }
        throw throw NoBreedReturnedFromDatabaseException("There were no breeds without sub breeds, returned from database")
    }

    suspend fun getOnlySubBreeds(): Iterable<String> {
        val result = repository.getOnlySubBreeds().toList()
        if(result.isNotEmpty()){
            return result
        }
        throw throw NoBreedReturnedFromDatabaseException("There were no sub breeds, returned from database")
    }

    suspend fun getBreedsSubBreeds(breed: String): Iterable<String> {
        if(breed.isNotEmpty()) {
            val result = repository.getBreedsSubBreeds(breed).toList()
            if(result.isNotEmpty()){
                return result
            }
            throw throw NoBreedReturnedFromDatabaseException("There were no sub breeds, for given breed, returned from database")
        }
        throw IllegalArgumentException("Wrong parameter submitted")
    }

    suspend fun getBreedPicture(breed: String): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        val dbBreed = repository.getBreedByName(breed) ?: throw IllegalArgumentException("Wrong parameter submitted")
        return if (dbBreed.image!=null) {
            ResponseEntity<ByteArray>(dbBreed.image, headers, HttpStatus.OK)
        } else {
            val responseImageFromAPI = apiClient.getBreedPictureFromExternalApiAndSaveNewLinkInDB(breed)
            if (responseImageFromAPI!=null) {
                ResponseEntity<ByteArray>(
                    responseImageFromAPI,
                    headers,
                    HttpStatus.OK
                )
            }else{
                throw NoPictureReturnedFromExternalSource("No picture was returned from external api, and nothing was found inside database.")
            }
        }
    }

    suspend fun saveDogBreed(dogBreed: DogBreedDTO): HttpStatus {
        try {
            repository.save(
                DogBreed(
                    breed = dogBreed.breed,
                    subBreed = dogBreed.subBreed.joinToString(separator = ", "),
                    image = null
                )
            )
        }catch (e: Exception){
            throw DatabaseSaveException("Error occurred while trying to save entity into database")
        }
        return HttpStatus.OK
    }
}