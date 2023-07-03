package com.epam.mentoring.kotlin.dogbreedapi.data

import com.fasterxml.jackson.annotation.JsonInclude
//import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.data.relational.core.mapping.Column
import jakarta.persistence.NamedQuery
//import jakarta.persistence.Table
import org.springframework.data.relational.core.mapping.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.util.*


//    @Entity
    @Table(name = "dog_breed")
    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @NamedQuery(
//        name = "Note.findByTitle",
//        query = "SELECT n FROM dog_breed n WHERE n.breed LIKE :breed"
//    )
    class DogBreed(
//        @Id
//        @GeneratedValue(generator = "uuid2")
//        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(value = "id")
        var id: String = UUID.randomUUID().toString(),
        @Column(value = "breed")
        var breed: String,
        @Column(value = "sub_breed")
        var subBreed: String,
        @Column(value = "image")
        var image: String = "",
//        @CreationTimestamp
//        var created: Date = Date(),
//        @UpdateTimestamp
//        var modified: Date = Date()
    ){

        constructor(): this(
            "","","", ""
        )

//        fun getId(): String{
////            val result: Boolean = Objects.isNull(id)
//            return if (Objects.isNull(id)) UUID.randomUUID().toString() else id!!
//        }
    }