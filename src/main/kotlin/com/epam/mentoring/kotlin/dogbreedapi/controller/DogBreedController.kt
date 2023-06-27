package com.epam.mentoring.kotlin.dogbreedapi.controller

import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.service.DogBreedService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/breeds")
class DogBreedController {

    @Autowired
    lateinit var service: DogBreedService

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllDogBreeds() = service.getBreeds().map { DogBreedResponse(it.breed, it.getSubBreeds(), it.image) }

    @GetMapping(
        value = ["/nonSubBreed"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getBreedsWithNoSubBreeds() = service.getBreedsWithNoSubBreeds()

    @GetMapping(
        value = ["/subBreeds"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getOnlySubBreeds() = service.getOnlySubBreeds()

    @GetMapping(
        value = ["/subBreedsForSpecificBreed/{breed}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getBreedsSubBreeds(@PathVariable(name = "breed") breed: String): Iterable<String> {
        return service.getBreedsSubBreeds(breed)
    }

//    @GetMapping(
//        value = ["/picture/"],
//        produces = [MediaType.APPLICATION_JSON_VALUE]
//    )fun getBreedPicture(): String{
//        return apiClient.getPicture().toString()
//    }

//    @GetMapping(
//        value = ["/getPicture/{breed}"]
//    )fun getBreedPicture(@PathVariable breed: String): ResponseEntity<ByteArray>{
//        return service.getPicture(breed)
////        return apiClient.getPicture().toString()
//    }

    @GetMapping(
        value = ["/getPicture/{breed}"]
    )
    suspend fun getBreedPicture2(@PathVariable breed: String): ResponseEntity<ByteArray>{
        return service.getBreedPicture(breed)
    }


}
