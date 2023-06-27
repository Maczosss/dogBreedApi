package com.epam.mentoring.kotlin.dogbreedapi.data_populator

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@Service
class DogBreedApiClient {

    @Autowired
    private lateinit var repository: DogBreedRepository


    fun getBreeds(): Map<String, List<String>> {
            val webClient = WebClient.create()
            val url = "https://dog.ceo/api/breeds/list/all"
            var dbBreeds: List<DogBreed>
            var result: Map<String, List<String>> = mutableMapOf()

            val responseMono = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(DogBreedApiResponse::class.java)

            responseMono.subscribe { dogApiResponse ->
                if (dogApiResponse.status == "success") {
                    result = dogApiResponse.message
                    dbBreeds = dogApiResponse.message.map {
                    DogBreed(
                            breed = it.key,
                            subBreed = it.value.joinToString(", ")
                        )
                    }
                    repository.saveAll(dbBreeds)
                }
            }
        return result
    }

    suspend fun getBreedPicture(breed: String): ByteArray {
        val webClient = WebClient.create()

        val listOfLinks = webClient.get().uri("https://dog.ceo/api/breed/${breed}/images")
            .awaitExchange().awaitBody<BasicWebClientMessageResponse>()

        return webClient.get().uri(listOfLinks.message[0])
            .awaitExchange().awaitBody<ByteArray>()
    }

    //wrong
    fun getPicture(breed: String): DogBreed {
        val webClient = WebClient.create()
        val url = "https://dog.ceo/api/breed/${breed}/images"
        var result: List<String> = mutableListOf() // links for images

        val responseMono = webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(BasicWebClientMessageResponse::class.java)

        responseMono.subscribe { dogApiResponse ->
            if (dogApiResponse.status == "success") {
                result = dogApiResponse.message
                val responseMono2 = webClient.get()
                    .uri(result[0])
                    .retrieve()
                    .bodyToMono(ByteArray::class.java)

                responseMono2.subscribe{imageResponse->
                    if (imageResponse.isNotEmpty()){
                        var dogBreed = repository.getBreedByName(breed)
                        dogBreed.image = imageResponse
                        dogBreed = repository.save(dogBreed)
                    }
                }
//                dbBreeds = dogApiResponse.message.map {
//                    DogBreed(
//                        breed = it.key,
//                        subBreed = it.value.joinToString(", ")
//                    )
//                }
//                repository.saveAll(dbBreeds)
            }
        }
        return DogBreed()
    }

}
