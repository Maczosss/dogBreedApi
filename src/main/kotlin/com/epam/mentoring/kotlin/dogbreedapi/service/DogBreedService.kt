package com.epam.mentoring.kotlin.dogbreedapi.service

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreedDTO
import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Component

@Component
class DogBreedService {

    @Autowired(required = true)
    lateinit var repository: DogBreedRepository

    @Autowired
    lateinit var apiClient: DogBreedApiClient

    suspend fun saveMapOfBreeds(breedsToSave: Map<String, List<String>>) {
        repository.saveAll(breedsToSave.map {
            DogBreed(
                breed = it.key,
                subBreed = it.value.joinToString(", ")
            )
        })
    }

    @org.springframework.cache.annotation.Cacheable("breeds")
    suspend fun getBreeds(): Iterable<DogBreedDTO> {
        return repository.findAll().map { DogBreedDTO(it) }.toList()
    }

    @org.springframework.cache.annotation.Cacheable("nonSubBreed")
    suspend fun getBreedsWithNoSubBreeds(): Iterable<DogBreedDTO> {
        return repository.findAllBreedsWhereThereAreNoSubBreeds().map { DogBreedDTO(it) }.toList()
    }

    suspend fun getOnlySubBreeds(): Iterable<String> {
        return repository.getOnlySubBreeds().toList()
    }

    suspend fun getBreedsSubBreeds(breed: String): Iterable<String> {
        return repository.getBreedsSubBreeds(breed).toList()
    }

    suspend fun getBreedPicture(breed: String): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        val dbBreed = repository.getBreedByName(breed)
        return if (dbBreed.image.isNotEmpty()) {
            ResponseEntity<ByteArray>(apiClient.getBreedPictureFromExternalApi(dbBreed.image), headers, HttpStatus.OK)
        } else
            ResponseEntity<ByteArray>(
                apiClient.getBreedPictureFromExternalApiAndSaveNewLinkInDB(breed),
                headers,
                HttpStatus.OK
            )
    }

    fun <K, V> MutableMap<K, List<V>>.toDogBreed(key: String, value: List<V>) =
        DogBreed(breed = key, subBreed = value.joinToString(separator = ","))

    suspend fun saveDogBreed(dogBreed: DogBreedDTO): HttpStatus {
//        if (repository.getBreedByName(dogBreed.breed) != null) {
//            return HttpStatus.METHOD_NOT_ALLOWED
//        }
        repository.save(DogBreed(breed = dogBreed.breed, subBreed = dogBreed.subBreed.joinToString(separator = ", ")))
        return HttpStatus.OK
    }
}