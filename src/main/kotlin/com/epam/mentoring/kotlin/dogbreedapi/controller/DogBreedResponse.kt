package com.epam.mentoring.kotlin.dogbreedapi.controller

data class DogBreedResponse(val breed: String, val subBreeds: List<String>, val image: ByteArray?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DogBreedResponse

        if (breed != other.breed) return false
        if (subBreeds != other.subBreeds) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = breed.hashCode()
        result = 31 * result + subBreeds.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}