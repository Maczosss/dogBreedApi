package com.epam.mentoring.kotlin.dogbreedapi.service

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreedDTO
import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Component

@Component
class DogBreedService {

    @Autowired
    lateinit var repository: DogBreedRepository

    @Autowired
    lateinit var apiClient: DogBreedApiClient

    fun save(breedsToSave: Map<String, List<String>>) {
        repository.saveAll(breedsToSave.map {
            DogBreed(
                breed = it.key,
                subBreed = it.value.joinToString(", ")
            )
        })
    }

    @org.springframework.cache.annotation.Cacheable("breeds")
    fun getBreeds(): Iterable<DogBreedDTO> {
        return repository.findAll().map { DogBreedDTO(it) }
    }

    @org.springframework.cache.annotation.Cacheable("nonSubBreed")
    fun getBreedsWithNoSubBreeds(): Iterable<DogBreedDTO> {
        return repository.findAllBreedsWhereThereAreNoSubBreeds().map { DogBreedDTO(it) }
    }

    fun getOnlySubBreeds(): Iterable<String> {
        return repository.getOnlySubBreeds()
    }

    fun getBreedsSubBreeds(breed: String): Iterable<String> {
        return repository.getBreedsSubBreeds(breed)
    }

    suspend fun getBreedPicture(breed: String): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.IMAGE_JPEG
        val dbBreed = repository.getBreedByName(breed)
        return if (dbBreed.image.isNotEmpty()) {
            ResponseEntity<ByteArray>(apiClient.getBreedPictureFromExternalApi(dbBreed.image), headers, HttpStatus.OK)
        } else
            ResponseEntity<ByteArray>(apiClient.getBreedPictureFromExternalApiAndSaveNewLinkInDB(breed), headers, HttpStatus.OK)
    }

    fun <K, V> MutableMap<K, List<V>>.toDogBreed(key: String, value: List<V>) =
        DogBreedDTO(breed = key, subBreed = value.joinToString(", "), "")
}