package com.epam.mentoring.kotlin.dogbreedapi.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class BreedNotFoundException(
    override val message: String? = null,
    val id: Long = 1,
    val responseStatus: HttpStatus = HttpStatus.NOT_FOUND
) : Exception(message)

@ResponseStatus(value = HttpStatus.NO_CONTENT)
class NoBreedReturnedFromDatabaseException(
    override val message: String? = null,
    val id: Long = 2,
    val responseStatus: HttpStatus = HttpStatus.NO_CONTENT
) : Exception(message)

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
class NoPictureReturnedFromExternalSource(
    override val message: String? = null,
    val id: Long = 3,
    val responseStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : Exception(message)

@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
class DatabaseSaveException(
    override val message: String? = null,
    val id: Long = 4,
    val responseStatus: HttpStatus = HttpStatus.NOT_MODIFIED
) : Exception(message)