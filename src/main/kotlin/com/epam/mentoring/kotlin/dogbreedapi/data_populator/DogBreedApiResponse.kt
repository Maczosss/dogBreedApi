package com.epam.mentoring.kotlin.dogbreedapi.data_populator

data class DogBreedApiResponse(
    var status: String,
    var message: Map<String, List<String>>
){
    constructor(): this(
        "",
        mutableMapOf()
    )
}