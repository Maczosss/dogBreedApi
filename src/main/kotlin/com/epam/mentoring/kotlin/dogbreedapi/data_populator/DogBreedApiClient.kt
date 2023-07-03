package com.epam.mentoring.kotlin.dogbreedapi.data_populator

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody


@Service
class DogBreedApiClient {

    @Autowired
    private lateinit var repository: DogBreedRepository

    suspend fun populateDogBreedTable() {
        val webClient = WebClient.create()
        val dbEntries = webClient.get().uri("https://dog.ceo/api/breeds/list/all")
            .retrieve()
            .awaitBody<DogBreedApiResponse>()

        val response = dbEntries.message.toDogBreeds()

        for (dogBreed in response) {
            repository.save(dogBreed)
        }
    }

    private fun Map<String, List<String>>.toDogBreeds(): List<DogBreed> = this.map { it.toDogBreed() }


    private fun Map.Entry<String, List<String>>.toDogBreed(): DogBreed =
        DogBreed(breed = key.toString(), subBreed = value.joinToString(separator = ", "), image = null)

    suspend fun getBreedPictureFromExternalApiAndSaveNewLinkInDB(breed: String): ByteArray {
        val webClient = WebClient.create()
        val listOfLinks = webClient.get().uri("https://dog.ceo/api/breed/${breed}/images")
            .retrieve()
            .awaitBody<BasicWebClientMessageResponse>()

        //save link to database
        val newDogBreed = repository.getBreedByName(breed)
        newDogBreed.image = webClient.get().uri(listOfLinks.message[0])
            .retrieve().awaitBody<ByteArray>()
        repository.update(newDogBreed.id, newDogBreed.image!!)
        return newDogBreed.image!!
    }

    suspend fun getBreedPictureFromExternalApi(url: String): ByteArray {
        return WebClient.create().get().uri(url)
            .retrieve().awaitBody<ByteArray>()
    }
}




