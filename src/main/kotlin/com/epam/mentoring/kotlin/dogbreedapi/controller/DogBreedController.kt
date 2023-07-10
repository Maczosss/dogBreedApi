package com.epam.mentoring.kotlin.dogbreedapi.controller

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreedDTO
import com.epam.mentoring.kotlin.dogbreedapi.error.BreedNotFoundException
import com.epam.mentoring.kotlin.dogbreedapi.error.DatabaseSaveException
import com.epam.mentoring.kotlin.dogbreedapi.error.NoBreedReturnedFromDatabaseException
import com.epam.mentoring.kotlin.dogbreedapi.error.NoPictureReturnedFromExternalSource
import com.epam.mentoring.kotlin.dogbreedapi.service.DogBreedService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import kotlin.IllegalArgumentException


@Controller
@RestController
@RequestMapping("/breeds")
class DogBreedController(private val service: DogBreedService) {

    @PostMapping(
        value = ["/add"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun addNewBreed(@RequestBody breed: DogBreedDTO): ResponseEntity<DogBreedDTO> {
        return ResponseEntity(service.saveDogBreed(breed))
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Get all breeds with their sub breeds from database.",
        description = "REST Endpoint that returns all breeds.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK, Returns Dog Breeds",
                content = arrayOf(
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(
                            schema = Schema(
                                implementation = DogBreedResponse::class
                            )
                        )
                    )
                )
            ),
            ApiResponse(
                responseCode = "204",
                description = "No Content, Nothing was returned from database"
            )
        ],
    )
    suspend fun getAllDogBreeds(): ResponseEntity<List<DogBreedResponse>> = ResponseEntity(
        service.getBreeds().map { DogBreedResponse(it.breed, it.getSubBreeds().toList()) },
        HttpStatus.OK)

    @GetMapping(
        value = ["/nonSubBreed"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Get all breeds without any sub-breeds.",
        description = "REST Endpoint that returns all breeds, without sub-breeds.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK, Returns Dog Breeds",
                content = arrayOf(
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(
                            schema = Schema(
                                implementation = Iterable::class
                            )
                        )
                    )
                )
            ),
            ApiResponse(
                responseCode = "204",
                description = "No Content, Nothing was returned from database"
            )
        ],
    )
    suspend fun getBreedsWithNoSubBreeds() = service.getBreedsWithNoSubBreeds()

    @GetMapping(
        value = ["/subBreeds"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Get list of all sub-breeds names from all breeds.",
        description = "REST Endpoint that returns all sub-breeds.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK, Returns Sub-Breeds",
                content = arrayOf(
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(
                            schema = Schema(
                                implementation = Iterable::class
                            )
                        )
                    )
                )
            ),
            ApiResponse(
                responseCode = "204",
                description = "No Content, Nothing was returned from database"
            )
        ],
    )
    suspend fun getOnlySubBreeds() = service.getOnlySubBreeds()

    @GetMapping(
        value = ["/subBreedsForSpecificBreed/{breed}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Get list of all sub-breeds names for specific breed.",
        description = "REST Endpoint that returns all sub-breeds for specific breed.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK, Returns Sub-Breeds",
                content = arrayOf(
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(
                            schema = Schema(
                                implementation = Iterable::class
                            )
                        )
                    )
                )
            ),
            ApiResponse(
                responseCode = "204",
                description = "No Content, Nothing was returned from database"
            )
        ],
    )
    suspend fun getBreedsSubBreeds(@PathVariable(name = "breed") breed: String): Iterable<String> {
        return service.getBreedsSubBreeds(breed.trim())
    }

    @GetMapping(
        value = ["/getPicture/{breed}"]
    )
    @Operation(
        summary = "Get single picture of a dog from specified breed.",
        description = "REST Endpoint that returns picture of a dog.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK, Returns Sub-Breeds",
                content = arrayOf(
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(
                            schema = Schema(
                                implementation = ByteArray::class
                            )
                        )
                    )
                )
            ),
            ApiResponse(
                responseCode = "204",
                description = "No Content, Nothing was returned from database"
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        ],
    )
    suspend fun getBreedPicture(@PathVariable breed: String): ResponseEntity<ByteArray> {
        return service.getBreedPicture(breed.trim())
    }

    //exception handling
    @ExceptionHandler(ResponseStatusException::class)
    fun handleIllegalArgOrBadRequest2(e: ResponseStatusException): ResponseEntity<String> =
        ResponseEntity("Wrong path", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgOrBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity("${e.message}", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity("nothing found ${e.message}", HttpStatus.NOT_FOUND)

    @ExceptionHandler(BreedNotFoundException::class)
    fun handleBreedNotFound(e: BreedNotFoundException): ResponseEntity<String> =
        ResponseEntity("${e.message}", e.responseStatus)

    @ExceptionHandler(NoBreedReturnedFromDatabaseException::class)
    fun handleBreedNotReturnedFromDB(e: NoBreedReturnedFromDatabaseException): ResponseEntity<String> =
        ResponseEntity("${e.message}", e.responseStatus)

    @ExceptionHandler(NoPictureReturnedFromExternalSource::class)
    fun handleNoPictureFoundOrDownloaded(e: NoPictureReturnedFromExternalSource): ResponseEntity<String> =
        ResponseEntity("${e.message}", e.responseStatus)

    @ExceptionHandler(DatabaseSaveException::class)
    fun handleDatabaseSaveOrUpdateException(e: DatabaseSaveException): ResponseEntity<String> =
        ResponseEntity("${e.message}", e.responseStatus)

    //    @ExceptionHandler(Exception::class)
//    fun handleExceptions(e: Exception): ResponseEntity<String> {
//        val result = when (true) {
//            (e is IllegalArgumentException) -> {ResponseEntity(
//                "${e.message}",
//                HttpStatus.BAD_REQUEST)}
//
//            (e is NoSuchElementException) -> {ResponseEntity(
//                "nothing found ${e.message}",
//                HttpStatus.NOT_FOUND)}
//
//            (e is BreedNotFoundException) -> {ResponseEntity(
//                "${e.message}",
//                HttpStatus.NOT_FOUND)}
//
//            (e is NoBreedReturnedFromDatabaseException) -> {ResponseEntity(
//                "${e.message}",
//                 HttpStatus.NO_CONTENT
//            )}
//
//            (e is NoPictureReturnedFromExternalSource) -> {ResponseEntity(
//                "${e.message}",
//                HttpStatus.INTERNAL_SERVER_ERROR
//            )}
//
//            (e is DatabaseSaveException) -> {ResponseEntity(
//                "${e.message}",
//                HttpStatus.NOT_MODIFIED
//            )}
//            else -> {
//                ResponseEntity("unknown error occurred.", HttpStatus.INTERNAL_SERVER_ERROR)
//            }
//        }
//
//        return ResponseEntity(result.body, result.statusCode)
//    }
}
