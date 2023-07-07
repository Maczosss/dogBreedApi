package com.epam.mentoring.kotlin.dogbreedapi.controller

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreedDTO
import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.service.DogBreedService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@OpenAPIDefinition(
    info = Info(
        title = "Hello World",
        version = "0.0",
        description = "My API",
        license = License(name = "Apache 2.0", url = "https://foo.bar"),
        contact = Contact(url = "https://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
    )
)
@Controller
@RestController
@RequestMapping("/breeds")
class DogBreedController(private val service: DogBreedService) {

    @PostMapping(
        value = ["/add"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun addNewBreed(@RequestBody breed: DogBreedDTO): ResponseEntity<DogBreedDTO>{
       return ResponseEntity(service.saveDogBreed(breed))
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun getAllDogBreeds() = service.getBreeds().map { DogBreedResponse(it.breed, it.getSubBreeds().toList()) }

    @GetMapping(
        value = ["/nonSubBreed"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun getBreedsWithNoSubBreeds() = service.getBreedsWithNoSubBreeds()

    @GetMapping(
        value = ["/subBreeds"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun getOnlySubBreeds() = service.getOnlySubBreeds()

    @GetMapping(
        value = ["/subBreedsForSpecificBreed/{breed}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun getBreedsSubBreeds(@PathVariable(name = "breed") breed: String): Iterable<String> {
        return service.getBreedsSubBreeds(breed.trim())
    }

    @GetMapping(
        value = ["/getPicture/{breed}"]
    )
    suspend fun getBreedPicture(@PathVariable breed: String): ResponseEntity<ByteArray>{
        return service.getBreedPicture(breed.trim())
    }
}
