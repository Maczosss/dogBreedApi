package com.epam.mentoring.kotlin.dogbreedapi.data

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Dog breed")
class DogBreedDTO (
    @field:Schema(
        description = "name of a dog breed",
        type = "String"
    )
    var breed: String,
    @field:Schema(
        description = "array of all sub breeds of main breed",
        type = "Array<String>"
    )
    var subBreed: Array<String>,
    @field:Schema(
        description = "picture of one specimen from this breed",
        type = "ByteArray"
    )
    var image: ByteArray?) {
    var id: String = ""

    constructor(dogBreed: DogBreed) : this(
        dogBreed.breed,
        dogBreed.subBreed.split(",")
            .map { it.trim() }
            .toTypedArray(),
        dogBreed.image
    ) {
        id = dogBreed.id
    }

    fun getSubBreeds() = subBreed
}