package com.epam.mentoring.kotlin.dogbreedapi.data

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*


    @Table(name = "dog_breed")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class DogBreed(
        @Column(value = "id")
        var id: String = UUID.randomUUID().toString(),
        @Column(value = "breed")
        var breed: String,
        @Column(value = "sub_breed")
        var subBreed: String,
        @Column(value = "image")
        var image: ByteArray?,
    ){

        constructor(): this(
            "","","", null
        )
    }