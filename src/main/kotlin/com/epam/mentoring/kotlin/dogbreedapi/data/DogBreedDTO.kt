package com.epam.mentoring.kotlin.dogbreedapi.data

import java.util.*

class DogBreedDTO (
    var breed: String,
    var subBreed: String,
    var image: ByteArray) {
    var id: String = ""
    var created: Date = Date()
    var modified: Date = Date()

    constructor(dogBreed: DogBreed) : this(
        dogBreed.breed,
        dogBreed.subBreed,
        dogBreed.image
    ) {
        id = dogBreed.id
        created = dogBreed.created
    }

    fun getSubBreeds() = subBreed.split(",").map { it.trim() }.toList()
}