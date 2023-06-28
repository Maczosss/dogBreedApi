package com.epam.mentoring.kotlin.dogbreedapi.controller

data class DogBreedResponse(val breed: String, val subBreeds: List<String>, val image: String)