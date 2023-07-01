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


    fun getBreeds(): DogBreedApiResponse {
        val webClient = WebClient.create()
        val url = "https://dog.ceo/api/breeds/list/all"
        var dbBreeds: List<DogBreed>
        var result: Map<String, List<String>> = mutableMapOf()
        var finalResult: DogBreedApiResponse = DogBreedApiResponse()

        val responseMono = webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(DogBreedApiResponse::class.java)

        responseMono.subscribe { dogApiResponse ->
            if (dogApiResponse.status == "success") {
                result = dogApiResponse.message
                finalResult = dogApiResponse
                dbBreeds = dogApiResponse.message.map {
                    DogBreed(
                        breed = it.key,
                        subBreed = it.value.joinToString(", ")
                    )
                }
                repository.saveAll(dbBreeds)
            }
        }
        return finalResult
    }

    suspend fun getBreedPictureFromExternalApiAndSaveNewLinkInDB(breed: String): ByteArray {
        val webClient = WebClient.create()
        val listOfLinks = webClient.get().uri("https://dog.ceo/api/breed/${breed}/images")
            .retrieve()
            .awaitBody<BasicWebClientMessageResponse>()
        //save link to database
        val newDogBreed = repository.getBreedByName(breed)
        newDogBreed.image = listOfLinks.message[0]

//        repository.save(newDogBreed)
        return webClient.get().uri(listOfLinks.message[0])
            .retrieve().awaitBody<ByteArray>()
    }

    suspend fun getBreedPictureFromExternalApi(url: String): ByteArray {
        return WebClient.create().get().uri(url)
            .retrieve().awaitBody<ByteArray>()
    }

    //wrong approach
//    fun getPicture(breed: String): DogBreed {
//        val webClient = WebClient.create()
//        val url = "https://dog.ceo/api/breed/${breed}/images"
//        var result: List<String> = mutableListOf() // links for images
//
//        val responseMono = webClient.get()
//            .uri(url)
//            .retrieve()
//            .bodyToMono(BasicWebClientMessageResponse::class.java)
//
//        responseMono.subscribe { dogApiResponse ->
//            if (dogApiResponse.status == "success") {
//                result = dogApiResponse.message
//                val responseMono2 = webClient.get()
//                    .uri(result[0])
//                    .retrieve()
//                    .bodyToMono(ByteArray::class.java)
//
//                responseMono2.subscribe{imageResponse->
//                    if (imageResponse.isNotEmpty()){
//                        var dogBreed = repository.getBreedByName(breed)
////                        dogBreed.image = imageResponse
//                        dogBreed = repository.save(dogBreed)
//                    }
//                }
////                dbBreeds = dogApiResponse.message.map {
////                    DogBreed(
////                        breed = it.key,
////                        subBreed = it.value.joinToString(", ")
////                    )
////                }
////                repository.saveAll(dbBreeds)
//            }
//        }
//        return DogBreed()
//    }

}
