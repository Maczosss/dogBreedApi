package com.epam.mentoring.kotlin.dogbreedapi.data

class DogBreedDTO (
    var breed: String,
    var subBreed: Array<String>,
    var image: ByteArray?) {
    var id: String = ""
//    var created: Date = Date()
//    var modified: Date = Date()

    constructor(dogBreed: DogBreed) : this(
        dogBreed.breed,
        dogBreed.subBreed.split(",").toTypedArray(),
        dogBreed.image
    ) {
        id = dogBreed.id
    }

    fun getSubBreeds() = subBreed
}